package com.catowl.service.impl;

import com.catowl.entity.Article;
import com.catowl.exception.BadRequestException;
import com.catowl.exception.InternetServerException;
import com.catowl.mapper.ArticleMapper;
import com.catowl.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ArticleServiceImpl implements ArticleService {
    private static final String LIKE_KEY_PREFIX = "article:like:user:";
    private static final String COUNT_KEY_PREFIX = "article:likeCount:";
    private static final String SYNCED_KEY_PREFIX = "article:like:synced:";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Autowired
    private ArticleMapper mapper;

    public Map<String,Object> getArticleById(Long article_id){
        try {
            return mapper.getArticleById(article_id);
        }catch (RuntimeException e){
            throw new InternetServerException("查询文章失败，查询数据库时发生异常");
        }
    }

    public List<Map<String, Object>> getArticlesByLike(int page, int pageSize) {
        try {
            int offset = (page - 1) * pageSize;
            List<Map<String, Object>> articles = mapper.articleListByLike(pageSize, offset);
            if (articles.isEmpty()) {
                return null;
            } else {
                return articles;
            }
        } catch (RuntimeException e) {
            throw new InternetServerException("查询失败，数据库出现异常");
        }
    }

    public List<Map<String, Object>> getArticlesByView(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        try {
            List<Map<String, Object>> articles = mapper.articleListByView(pageSize, offset);
            if (articles.isEmpty()) {
                return null;
            } else {
                return articles;
            }
        } catch (RuntimeException e) {
            throw new InternetServerException("查询失败，数据库出现异常");
        }
    }

    public void incrementViewCount(Long articleId) {
        try{
            mapper.incrementViewCount(articleId);
        }catch (RuntimeException e){
            throw new InternetServerException("访问失败，数据库出现异常");
        }
    }

    public int deleteArticle(Long articleId) {
        try{
            //删除redis里的点赞信息缓存，防止脏数据
            deleteArticleLikeCache(articleId);
            return mapper.deleteArticle(articleId);
        }catch (RuntimeException e){
            throw new InternetServerException("删除文章失败，数据库更新时出现异常");
        }
    }

    private void deleteArticleLikeCache(Long articleId) {
        // 文章的用户点赞集合Key
        String likeKey = LIKE_KEY_PREFIX + articleId;

        // 文章的点赞总数Key
        String countKey = COUNT_KEY_PREFIX + articleId;

        // 一起删除
        redisTemplate.delete(likeKey);
        redisTemplate.delete(countKey);
    }

    public void publishArticle(Article article) {
        try{
            mapper.publishArticle(article);
        }catch (RuntimeException e){
            throw new InternetServerException("发表文章失败，数据库存储时出现异常");
        }
    }

}
