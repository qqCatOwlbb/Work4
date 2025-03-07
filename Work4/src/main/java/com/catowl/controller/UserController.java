package com.catowl.controller;

import com.catowl.entity.Article;
import com.catowl.entity.User;
import com.catowl.exception.BadRequestException;
import com.catowl.service.UserService;
import com.catowl.utils.APIResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class UserController {
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

    @Autowired
    private UserService userService;

    @GetMapping("/user/getart")
    public ResponseEntity<List<Map<String,Object>>> myArticle(){
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<>(userService.selectArticleByAuthor(userId),HttpStatus.OK);
    }

    @GetMapping("/user/mylike")
    public ResponseEntity<List<Map<String,Object>>> myLike(){
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<>(userService.selectMyLike(userId),HttpStatus.OK);
    }

    //这是之前给comment查询用户头像姓名的，现在comment的查询采用了联表查询，所以暂时没用了
    @GetMapping("user/getinfobyid")
    public ResponseEntity<User> getInfoById(@RequestParam("id") Long id){
        return new ResponseEntity<>(userService.getInfoById(id),HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        if(user.getUsername()==null||user.getPassword()==null){
            throw new BadRequestException("用户名或密码不能为空");
        }
        String token=userService.login(user);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user){
        if((user.getUsername()==null||user.getUsername().isEmpty())||(user.getPassword()==null||user.getPassword().isEmpty())){
            throw new BadRequestException("用户名或密码不能为空");
        }
        userService.insertUser(user);
        return new ResponseEntity<>("注册成功",HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody User user){
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        user.setId(userId);
        userService.updateUserInfo(user);
        boolean isUsernameModified = user.getUsername() != null && !user.getUsername().isEmpty();
        boolean isPasswordModified = user.getPassword() != null && !user.getPassword().isEmpty();
        if (isUsernameModified || isPasswordModified) {
            return new ResponseEntity<>("更新成功，用户名或密码已修改，请重新登录",HttpStatus.OK);
        } else {
            return new ResponseEntity<>("更新成功",HttpStatus.OK);
        }
    }

    @PostMapping("/updateavatar")
    public ResponseEntity<String> updateAvatar(@RequestParam(value = "file", required = false) MultipartFile file){
        if(file!=null&&file.isEmpty()){
            if(file.getSize() > MAX_FILE_SIZE){
                throw new BadRequestException("文件过大，最大允许2MB");
            }
        }
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        User user=new User();
        user.setId(userId);
        String newAvatar = userService.handleAvatarUpload(userId,file);
        user.setAvatar(newAvatar);
        userService.updateUserInfo(user);
        return new ResponseEntity<>("更新头像成功",HttpStatus.OK);
    }

    @GetMapping("/user/getinfo")
    public ResponseEntity<User> getinfo(){
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        User user=userService.selectUser(userId);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }
}
