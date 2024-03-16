package com.ceos19.springboot.post.entity;

import com.ceos19.springboot.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name ="post")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String title;

    private String content;

    private Boolean anonymous;

    private Integer view;

    private Integer likes;

}
