package com.ceos19.springboot.reply.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "reply")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    private String content;

    private Integer likes;
}
