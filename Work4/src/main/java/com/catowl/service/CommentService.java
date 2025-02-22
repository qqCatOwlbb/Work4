package com.catowl.service;

import com.catowl.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    public int insertComment(Long article_id,Long user_id,String content,Long parent_comment_id) throws Exception{
        return commentMapper.addComment(article_id,user_id,content,parent_comment_id);
    }

    public List<Map<String, Object>> selectParentComment(Long article_id){
        return commentMapper.selectParentComment(article_id);
    }

    public List<Map<String,Object>> selectSonComment(Long parent_comment_id){
        return commentMapper.selectSonComment(parent_comment_id);
    }
}
