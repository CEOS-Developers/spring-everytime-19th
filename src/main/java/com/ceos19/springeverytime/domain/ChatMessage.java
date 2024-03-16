package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;

import java.util.Date;

public class ChatMessage {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 2000, nullable = false)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;

    @ManyToOne
    private ChatRoom room;

    @ManyToOne
    private User sender;
}
