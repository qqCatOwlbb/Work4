package com.catowl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LikeMapper {
    int addArticleLike(@Param("article_id") Long article_id, @Param("user_id") Long user_id);
    int incrementLikeCount(@Param("article_id") Long article_id);
    int deleteArticleLike(@Param("article_id") Long article_id, @Param("user_id") Long user_id);
    int decrementLikeCount(@Param("article_id") Long article_id);
}
