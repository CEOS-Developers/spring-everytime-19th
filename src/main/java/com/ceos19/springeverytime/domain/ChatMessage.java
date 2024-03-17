package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;

import java.util.Date;

public class ChatMessage {
    @Id
    @GeneratedValue
    private Long messageId;

    @Column(length = 2000, nullable = false)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender;
}
