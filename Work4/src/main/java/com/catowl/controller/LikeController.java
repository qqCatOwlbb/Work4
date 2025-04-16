package com.catowl.controller;

import com.catowl.entity.User;
import com.catowl.service.LikeService;
import com.catowl.utils.APIResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PostMapping("/article/like")
    public ResponseEntity<String> addLike(@RequestParam("article_id") Long article_id){
        likeService.likeArticle(article_id);
        return new ResponseEntity<>("点赞成功", HttpStatus.OK);
    }

    @DeleteMapping("/article/like")
    public ResponseEntity<String> deleteLike(@RequestParam("article_id") Long article_id){
        likeService.unlikeArticle(article_id);
        return new ResponseEntity<>("取消点赞成功",HttpStatus.OK);
    }

    @GetMapping("/article/getlike")
    public ResponseEntity<Integer> getLike(@RequestParam("article_id") Long article_id){
        int likeCount=likeService.getArticleLikeCount(article_id);
        return new ResponseEntity<>(likeCount,HttpStatus.OK);
    }
}

