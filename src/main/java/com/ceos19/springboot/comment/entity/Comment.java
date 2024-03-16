package com.ceos19.springboot.comment.entity;

import com.ceos19.springboot.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String content;

    private Integer contentLike;
}
