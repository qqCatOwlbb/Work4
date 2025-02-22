package com.catowl.controller;

import com.catowl.service.ArticleService;
import com.catowl.utils.APIResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/article/select/bylike")
    public Object selArtcleListByLike(@RequestParam("page") int page,
                                      @RequestParam("pageSize") int pageSize) {
        try {
            List<Map<String,Object>> articles=articleService.getArticlesByLike(page,pageSize);
            return APIResultUtil.apiResult(true,articles,"读取成功");
        } catch (Exception e) {
            System.out.println("程序错误！错误原因：" + e);
            return APIResultUtil.apiResult(false, null, "程序错误");
        }
    }

    @GetMapping("/article/select/byview")
    public Object selArtcleListByView(@RequestParam("page") int page,
                                      @RequestParam("pageSize") int pageSize) {
        try {
            List<Map<String,Object>> articles=articleService.getArticlesByView(page,pageSize);
            return APIResultUtil.apiResult(true,articles,"读取成功");
        } catch (Exception e) {
            System.out.println("程序错误！错误原因：" + e);
            return APIResultUtil.apiResult(false, null, "程序错误");
        }
    }

    @PostMapping("/article/view")
    public Object incrementViewCount(@RequestParam("article_id") Long articleId){
        try{
            int i=articleService.incrementViewCount(articleId);
            if(i>0){
                return APIResultUtil.apiResult(true,null,"访问成功");
            }else{
                return APIResultUtil.apiResult(false,null,"访问失败");
            }
        } catch (Exception e) {
            System.out.println("错误原因："+e);
            return APIResultUtil.apiResult(false,null,"程序错误");
        }
    }

    @PostMapping("/article/publish")
    public Object publishArticle(@RequestParam("title") String title,@RequestParam("content") String content,@RequestParam("author_id") Long author_id){
        try {
            int i=articleService.publishArticle(title,content,author_id);
            if(i>0){
                return APIResultUtil.apiResult(true,null,"发布成功");
            }else{
                return  APIResultUtil.apiResult(false,null,"发布失败");
            }
        }catch (Exception e){
            System.out.println("程序错误，原因是："+e);
            return APIResultUtil.apiResult(false,null,"程序错误");
        }
    }

    @DeleteMapping("article/delete")
    public Object deleteArticle(@RequestParam("article_id") Long article_id){
        try{
            int i=articleService.deleteArticle(article_id);
            if(i>0){
                return APIResultUtil.apiResult(true,null,"删除成功");
            }else{
                return  APIResultUtil.apiResult(false,null,"删除失败");
            }
        }catch (Exception e){
            System.out.println("程序错误，原因是："+e);
            return APIResultUtil.apiResult(false,null,"程序错误");
        }
    }

}
