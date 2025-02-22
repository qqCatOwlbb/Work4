package com.catowl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommentMapper {
    int addComment(@Param("article_id") Long article_id,@Param("user_id") Long user_id,@Param("content") String content, @Param("parent_comment_id") Long parent_comment_id);
    List<Map<String,Object>> selectParentComment(@Param("article_id") Long article_id);
    List<Map<String,Object>> selectSonComment(@Param("parent_comment_id") Long parentId);
}
