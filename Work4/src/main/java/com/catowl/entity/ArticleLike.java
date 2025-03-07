package com.catowl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLike {
    private Long id;  // 主键ID

    private Long article_id;  // 文章ID（外键）
    private Long user_id;  // 用户ID（外键）

    private LocalDateTime created_at;  // 点赞时间
}
