package com.catowl.controller;

import com.catowl.entity.Article;
import com.catowl.entity.User;
import com.catowl.exception.BadRequestException;
import com.catowl.service.ArticleService;
import com.catowl.utils.APIResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/article/select/bylike")
    public ResponseEntity<List<Map<String, Object>>> selArtcleListByLike(@RequestParam("page") int page,
                                                                         @RequestParam("pageSize") int pageSize) {
        if (page < 1 || pageSize < 1) {
            throw new BadRequestException("页码与页面大小应为正整数");
        }
        List<Map<String, Object>> articles = articleService.getArticlesByLike(page, pageSize);
        if (articles == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/article/select/byview")
    public Object selArtcleListByView(@RequestParam("page") int page,
                                      @RequestParam("pageSize") int pageSize) {
        if (page < 1 || pageSize < 1) {
            throw new BadRequestException("页码与页面大小应为正整数");
        }
        List<Map<String, Object>> articles = articleService.getArticlesByView(page, pageSize);
        if (articles == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @PostMapping("/article/view")
    public ResponseEntity<Map<String,Object>> getById(@RequestParam("article_id") Long articleId) {
        Map<String,Object> article=articleService.getArticleById(articleId);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @PostMapping("/article/publish")
    public ResponseEntity<String> publishArticle(@RequestBody Article article) {
        if ((article.getTitle() == null || article.getTitle().isEmpty()) || article.getContent() == null || article.getContent().isEmpty()) {
            throw new BadRequestException("文章标题和内容不能为空");
        }
        articleService.publishArticle(article);
        return new ResponseEntity<>("文章发表成功", HttpStatus.OK);
    }

    @DeleteMapping("/article/delete")
    public ResponseEntity<String> deleteArticle(@RequestParam("article_id") Long article_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = (User) authentication.getPrincipal();
        Long userId = loginUser.getId();
        Map<String, Object> map = articleService.getArticleById(article_id);
        Long author_id = (Long) map.get("author_id");
        if (!userId.equals(author_id)) {
            throw new BadRequestException("仅能删除自己写的文章");
        }
        articleService.deleteArticle(article_id);
        return new ResponseEntity<>("文章删除成功", HttpStatus.OK);
    }

}
