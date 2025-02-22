package com.catowl.service;

import com.catowl.entity.User;
import com.catowl.entity.UserUpdateRequest;
import com.catowl.mapper.UserMapper;
import com.catowl.utils.APIResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User selectUser(String username){
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        return userMapper.selectUser(user.getId());
    }

    public List<Map<String,Object>> selectArticleByAuthor(Long authorId){
        return userMapper.selectArticleByAuthor(authorId);
    }

    public List<Map<String,Object>> selectMyLike(Long UserId){
        return userMapper.selectMyLike(UserId);
    }

    public int insertUser(String username,String password){
        User user = userMapper.findByUsername(username);
        if (user != null) {
            throw new UsernameNotFoundException("用户名已存在: " + username);
        }
        return userMapper.insertUser(username,password);
    }

    public int updateUserInfo(Long id,String username,String password,String avatar,String bio){
        if(username!=null){
            User user = userMapper.findByUsername(username);
            if (user != null) {
                throw new UsernameNotFoundException("用户名已存在");
            }
        }
        return userMapper.updateUser(id,username,password,avatar,bio);
    }

    public User getInfoById(Long id){
        return userMapper.getinfobyid(id);
    }
}
