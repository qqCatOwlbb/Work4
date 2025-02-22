package com.catowl.controller;

import com.catowl.entity.User;
import com.catowl.service.UserService;
import com.catowl.utils.APIResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/getart")
    public Object myArticle(@RequestParam("author_id")Long author_id){
        try{
            return APIResultUtil.apiResult(true,userService.selectArticleByAuthor(author_id),"获取我的文章");
        } catch (Exception e) {
            System.out.println("错误原因："+e);
            return APIResultUtil.apiResult(false,null,"获取失败");
        }
    }

    @GetMapping("/user/mylike")
    public Object myLike(@RequestParam("user_id") Long user_id){
        try{
            return APIResultUtil.apiResult(true,userService.selectMyLike(user_id),"获取我喜欢的文章");
        } catch (Exception e) {
            System.out.println("错误原因："+e);
            return APIResultUtil.apiResult(false,null,"程序错误");
        }
    }

    @GetMapping("user/getinfobyid")
    public Object getInfoById(@RequestParam("id") Long id){
        try{
            return APIResultUtil.apiResult(true,userService.getInfoById(id),"获取用户名称和头像");
        } catch (Exception e) {
            System.out.println(e);
            return APIResultUtil.apiResult(false,null,"获取失败");
        }
    }
}
