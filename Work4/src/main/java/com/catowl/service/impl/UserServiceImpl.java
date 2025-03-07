package com.catowl.service.impl;

import com.catowl.entity.User;
import com.catowl.exception.InternetServerException;
import com.catowl.exception.UnauthorizedException;
import com.catowl.mapper.ArticleMapper;
import com.catowl.mapper.UserMapper;
import com.catowl.service.UserService;
import com.catowl.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${file.upload-dir}")
    private String uploadDir;//上传的目录

    public User selectUser(Long userId) {
        try {
            return userMapper.selectUser(userId);
        } catch (RuntimeException e) {
            throw new InternetServerException("查询用户信息失败，数据库查询时发生异常");
        }
    }

    public List<Map<String, Object>> selectArticleByAuthor(Long authorId) {
        try {
            return userMapper.selectArticleByAuthor(authorId);
        } catch (RuntimeException e) {
            throw new InternetServerException("查询我的文章失败，数据库查询时发生异常");
        }
    }

    public List<Map<String, Object>> selectMyLike(Long UserId) {
        try {
            return userMapper.selectMyLike(UserId);
        } catch (RuntimeException e) {
            throw new InternetServerException("查询我喜欢的文章失败，数据库查询时发生异常");
        }
    }

    public int insertUser(User newUser) {
        User user = userMapper.findByUsername(newUser.getUsername());
        if (user != null) {
            throw new InternetServerException("用户名已存在: " + newUser.getUsername());
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userMapper.insertUser(newUser);
    }

    public int updateUserInfo(User newUser) {
        if (newUser.getUsername() != null && newUser.getUsername().isEmpty()) {
            User user = userMapper.findByUsername(newUser.getUsername());
            if (user != null) {
                throw new InternetServerException("用户名已存在");
            }
        }
        if (newUser.getPassword() != null && !newUser.getPassword().isEmpty()) {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }
        return userMapper.updateUser(newUser);
    }

    public User getInfoById(Long id) {
        return userMapper.getinfobyid(id);
    }

    public String login(User user) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            User userDetails = (User) userDetailsService.loadUserByUsername(user.getUsername());
            return jwtUtil.generateToken(userDetails.getId());
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("用户名或密码错误");
        }
    }

    public String handleAvatarUpload(Long userId, MultipartFile file) {
        User user = userMapper.selectUser(userId);
        String oldAvatarUrl = user.getAvatar();
        deleteOldAvatar(oldAvatarUrl);
        String fileName = generateFileName(file);
        Path filePath = Paths.get(uploadDir, fileName);
        try {
            Files.createDirectories(filePath.getParent());//确保目录存在
            Files.write(filePath, file.getBytes());//写入文件
        } catch (IOException e) {
            throw new InternetServerException("保存头像失败");
        }
        String newAvatarUrl = "/uploads/" + fileName;
        User newUser = new User();
        newUser.setId(userId);
        newUser.setAvatar(newAvatarUrl);
        userMapper.updateUser(newUser);
        return newAvatarUrl;
    }

    public String generateFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String fileExt = originalFilename.substring(originalFilename.lastIndexOf("."));
        return "avatar-" + UUID.randomUUID() + fileExt;
    }

    public void deleteOldAvatar(String oldAvatarUrl) {
        if (oldAvatarUrl != null &&oldAvatarUrl.startsWith("/uploads/")){
            String oldFileName=oldAvatarUrl.substring("/uploads/".length());
            Path oldFilePath=Paths.get(uploadDir,oldFileName);
            try{
                Files.deleteIfExists(oldFilePath);
            } catch (IOException e) {
                throw new InternetServerException("删除头像失败");
            }
        }
    }
}
