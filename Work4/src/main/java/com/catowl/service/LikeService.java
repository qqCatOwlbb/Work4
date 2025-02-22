package com.catowl.service;

import com.catowl.mapper.LikeMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {
    @Autowired
    private LikeMapper likeMapper;

    @Transactional
    public boolean addLike(Long articleId, Long userId) throws Exception {
        // 插入点赞记录
        int addedLike = likeMapper.addArticleLike(articleId, userId);
        if (addedLike == 0) {
            throw new RuntimeException("添加点赞记录失败");
        }

        // 更新文章点赞数
        int updatedLikes = likeMapper.incrementLikeCount(articleId);
        if (updatedLikes == 0) {
            throw new RuntimeException("更新点赞数失败");
        }

        return true; // 两个操作都成功，返回 true
    }

    @Transactional
    public boolean deleteLike(Long articleId, Long userId) throws Exception{
        int deleteLike=likeMapper.deleteArticleLike(articleId,userId);
        if(deleteLike==0){
            throw new RuntimeException("取消点赞失败");
        }
        int updateLikes = likeMapper.decrementLikeCount(articleId);
        if(updateLikes==0){
            throw new RuntimeException("更新点赞数失败");
        }

        return true;
    }
}
