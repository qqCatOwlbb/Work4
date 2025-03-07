package com.catowl.controller;

import com.catowl.entity.Comment;
import com.catowl.exception.BadRequestException;
import com.catowl.service.CommentService;
import com.catowl.utils.APIResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/comment/add")
    public ResponseEntity<String> addComment(@RequestBody Comment comment) {
        if (comment.getArticle_id() == null || comment.getContent() == null) {
            throw new BadRequestException("文章id和评论内容不能为空");
        }
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        comment.setUser_id(userId);
        commentService.insertComment(comment);
        return new ResponseEntity<>("评论发表成功", HttpStatus.OK);
    }

    @DeleteMapping("/comment/delete")
    public ResponseEntity<String> deleteComment(@RequestParam("comment_id") Long comment_id){
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        Map<String,Object> map=commentService.selectCommentById(comment_id);
        Long user_id=(Long) map.get("user_id");
        if(!userId.equals(user_id)){
            throw new BadRequestException("仅能删除自己的评论");
        }
        commentService.deleteComment(comment_id);
        return new ResponseEntity<>("评论删除成功",HttpStatus.OK);
    }

    @GetMapping("/comment/getById")
    public ResponseEntity<Map<String,Object>> selectCommentById(@RequestParam("comment_id") Long comment_id){
        return new ResponseEntity<>(commentService.selectCommentById(comment_id),HttpStatus.OK);
    }

    @GetMapping("/comment/parent")
    public ResponseEntity<List<Map<String, Object>>> selectParentComment(@RequestParam("article_id") Long article_id) {
        List<Map<String, Object>> maps = commentService.selectParentComment(article_id);
        return new ResponseEntity<>(maps, HttpStatus.OK);
    }

    @GetMapping("/comment/son")
    public ResponseEntity<List<Map<String, Object>>> selectSonComment(@RequestParam("parent_comment_id") Long parentId) {
        List<Map<String, Object>> maps = commentService.selectSonComment(parentId);
        return new ResponseEntity<>(maps, HttpStatus.OK);
    }

}
