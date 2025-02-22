package com.catowl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private Long id;  // 评论ID（主键）

    private Long article_id;  // 关联的文章ID（外键）
    private Long user_id;  // 发表评论的用户ID（外键）
    private String content;  // 评论内容

    private Long parent_comment_id;  // 父评论ID（如果是根评论，则为null）

    private LocalDateTime create_at;  // 创建时间
}
