package com.catowl.controller;

import com.catowl.service.CommentService;
import com.catowl.utils.APIResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/comment/add")
    public Object addComment(@RequestParam("article_id") Long article_id, @RequestParam("user_id") Long user_id, @RequestParam("content") String content, @RequestParam("parent_comment_id") @Nullable Long parent_comment_id){
        try {
            int i=commentService.insertComment(article_id, user_id, content, parent_comment_id);
            if(i>0){
                return APIResultUtil.apiResult(true,null,"评论成功");
            }else{
                return APIResultUtil.apiResult(false,null,"评论失败");
            }
        } catch (Exception e) {
            System.out.println("错误原因："+e);
            return APIResultUtil.apiResult(false,null,"程序错误，请输入存在的评论id，若其无父评论，请传入null");
        }
    }

    @GetMapping("/comment/parent")
    public Object selectParentComment(@RequestParam("article_id") Long article_id){
        try {
            List< Map<String,Object>> maps=commentService.selectParentComment(article_id);
            return APIResultUtil.apiResult(true,maps,"获取父评论成功");
        } catch (Exception e) {
            System.out.println("错误原因:"+e);
            return APIResultUtil.apiResult(false,null,"程序错误");
        }
    }

    @GetMapping("/comment/son")
    public Object selectSonComment(@RequestParam("parent_comment_id") Long parentId){
        try{
            List<Map<String,Object>> maps=commentService.selectSonComment(parentId);
            return APIResultUtil.apiResult(true,maps,"获取子评论成功");
        } catch (Exception e) {
            System.out.println("错误原因："+e);
            return APIResultUtil.apiResult(false,null,"获取子评论失败");
        }
    }
}
