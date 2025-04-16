package com.catowl.mapper;

import com.catowl.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    User selectUser(@Param("id")Long id);
    List<Map<String,Object>> selectArticleByAuthor(@Param("author_id") Long author_id);
    List<Map<String,Object>> selectMyLike(@Param("user_id") Long user_id);
    User findByUsername(@Param("username") String username);
    int insertUser(User user);
    int updateUser(User user);
    User getinfobyid(@Param("id")Long id);
}
