package com.catowl.service;


import com.catowl.entity.Comment;

import java.util.List;
import java.util.Map;

public interface CommentService {
    void insertComment(Comment comment);
    void deleteComment(Long comment_id);
    Map<String,Object> selectCommentById(Long comment_id);
    List<Map<String, Object>> selectParentComment(Long article_id);
    List<Map<String,Object>> selectSonComment(Long parent_comment_id);
}
