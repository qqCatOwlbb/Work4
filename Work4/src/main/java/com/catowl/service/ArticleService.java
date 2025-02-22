package com.catowl.service;

import com.catowl.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ArticleService {
    @Autowired
    private ArticleMapper mapper;

    public List<Map<String,Object>> getArticlesByLike(int page, int pageSize) throws Exception {
        int offset = (page - 1) * pageSize;
        List<Map<String,Object>> articles = mapper.articleListByLike(pageSize, offset);
//            这里判断是否有数据（注意是倒过来的）
        if (articles.isEmpty()) {
//                没有数据
            return null;
        } else {
//                有数据
            return articles;

        }
    }

    public List<Map<String,Object>> getArticlesByView(int page, int pageSize) throws Exception {
        int offset = (page - 1) * pageSize;
        List<Map<String,Object>> articles = mapper.articleListByView(pageSize, offset);
//            这里判断是否有数据（注意是倒过来的）
        if (articles.isEmpty()) {
//                没有数据
            return null;
        } else {
//                有数据
            return articles;

        }
    }

    public int incrementViewCount(Long articleId){
        return mapper.incrementViewCount(articleId);
    }

    public int deleteArticle(Long articleId){
        return mapper.deleteArticle(articleId);
    }

    public int publishArticle(String title,String content,Long author_id) throws Exception{
        int i = mapper.publishArticle(title,content,author_id);
        return i;
    }

}
