package com.catowl.service;

import com.catowl.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService {
    User selectUser(Long UserId);
    List<Map<String,Object>> selectArticleByAuthor(Long authorId);
    List<Map<String,Object>> selectMyLike(Long UserId);
    int updateUserInfo(User user);
    User getInfoById(Long id);
    String login(User user);
    int insertUser(User user);
    String handleAvatarUpload(Long userId,MultipartFile file);
    String generateFileName(MultipartFile file);
    void deleteOldAvatar(String oldAvatarUrl);
}
