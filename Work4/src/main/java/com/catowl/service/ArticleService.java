package com.catowl.service;

import com.catowl.entity.Article;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    List<Map<String, Object>> getArticlesByLike(int page, int pageSize);

    List<Map<String, Object>> getArticlesByView(int page, int pageSize);

    void incrementViewCount(Long articleId);

    int deleteArticle(Long articleId);

    void publishArticle(Article article);

    Map<String,Object> getArticleById(Long article_id);
}
