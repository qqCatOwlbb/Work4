package com.catowl.controller;

import com.catowl.service.LikeService;
import com.catowl.utils.APIResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LikeController {
    @Autowired
    private LikeService likeService;
    @PostMapping("/article/like")
    public Object addLike(@RequestParam("article_id") Long article_id,@RequestParam("user_id") Long user_id){
        try {
            boolean i=likeService.addLike(article_id,user_id);
            if(i) {
                return APIResultUtil.apiResult(true, null, "点赞成功");
            }else{
                return APIResultUtil.apiResult(false,null,"点赞失败");
            }
        }catch (Exception e){
            System.out.println("程序错误，原因是："+e);
            return APIResultUtil.apiResult(false,null,"程序错误");
        }
    }

    @DeleteMapping("/article/like")
    public Object deleteLike(@RequestParam("article_id") Long article_id,@RequestParam("user_id") Long user_id){
        try{
            boolean i=likeService.deleteLike(article_id,user_id);
            if(i){
                return APIResultUtil.apiResult(true,null,"取消成功");
            }else{
                return APIResultUtil.apiResult(false,null,"取消失败");
            }
        } catch (Exception e) {
            System.out.println("程序错误，原因是："+e);
            return APIResultUtil.apiResult(false,null,"程序错误");
        }
    }
}

