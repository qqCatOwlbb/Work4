package com.catowl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private Long id;  // 文章ID（主键）

    private String title;  // 文章标题
    private String content;  // 文章内容（Markdown 格式）
    private Long author_id;  // 作者ID（外键，关联 users 表）

    private Integer view_count;  // 文章浏览量
    private Integer like_count;  // 文章点赞量
    private Integer status;  // 文章状态（0=草稿，1=已发布）

    private LocalDateTime created_at;  // 创建时间
    private LocalDateTime updated_at;  // 更新时间
}
