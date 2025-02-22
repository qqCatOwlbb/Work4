package com.catowl.controller;

import com.catowl.entity.User;
import com.catowl.entity.UserUpdateRequest;
import com.catowl.mapper.UserMapper;
import com.catowl.service.UserService;
import com.catowl.utils.APIResultUtil;
import com.catowl.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private UserService userService; // MyBatis Mapper

    @Autowired
    private PasswordEncoder passwordEncoder; // Spring Security 加密工具


    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public Object login(@RequestParam("username") String username, @RequestParam("password") String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return APIResultUtil.apiResult(true, jwtUtil.generateToken(userDetails.getUsername()), "登录成功");
        } catch (BadCredentialsException e) {
            return APIResultUtil.apiResult(false,null,"用户名或密码错误");
        }
    }

    @PostMapping("/register")
    public Object register(@RequestParam("username") String username, @RequestParam("password") String password){
        try{
            userService.insertUser(username,passwordEncoder.encode(password));
            return APIResultUtil.apiResult(true,null,"注册成功，请重新登录");
        }catch (Exception e){
            return APIResultUtil.apiResult(false,null,"注册失败，"+e);
        }
    }

    @PostMapping("/update")
    public Object updateUserInfo(@RequestParam("id") Long id,
                                 @RequestParam("username") @Nullable String username,
                                 @RequestParam("password") @Nullable String password,
                                 @RequestParam("avatar") @Nullable String avatar,
                                 @RequestParam("bio") @Nullable String bio) {
        try{
            int i;
            if(password != null && !password.isEmpty()){
                i = userService.updateUserInfo(id, username, passwordEncoder.encode(password), avatar, bio);
            }else{
                i= userService.updateUserInfo(id, username, null, avatar, bio);
            }
            if (i > 0) {
                boolean isUsernameModified = username != null && !username.isEmpty();
                boolean isPasswordModified = password != null && !password.isEmpty();

                if (isUsernameModified || isPasswordModified) {
                    return APIResultUtil.apiResult(true, 100, "更新成功, 用户名或密码已修改，请重新登录");
                } else {
                    return APIResultUtil.apiResult(true, 200, "更新成功");
                }
            } else {
                return APIResultUtil.apiResult(false, null, "更新失败");
            }
        } catch (Exception e) {
            System.out.println("错误原因:"+e);
            return APIResultUtil.apiResult(false,300,"更新失败，可能原因："+e);
        }
    }

    @GetMapping("/user/getinfo")
    public Object selectUser(@RequestHeader("Authorization") String token) {
        try {
            // 去掉 "Bearer " 前缀，得到实际 Token
            String jwtToken = token.replace("Bearer ", "");

            // 解析 Token，获取用户名
            String username = jwtUtil.getUsernameFromToken(jwtToken);

            // 通过用户名查找用户信息
            User user=userService.selectUser(username);

            // 返回用户信息
            return APIResultUtil.apiResult(true, user, "获取用户信息成功");
        } catch (Exception e) {
            System.out.println("错误原因：" + e);
            return APIResultUtil.apiResult(false, null, "程序错误，获取用户信息失败"+e);
        }
    }

}
