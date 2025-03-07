package com.catowl.service;

public interface LikeService {
    boolean likeArticle(Long articleId,Long userId);
    boolean unlikeArticle(Long articleId,Long userId);
    void syncLikesToMySQL();
    int getArticleLikeCount(Long articleId);
}
