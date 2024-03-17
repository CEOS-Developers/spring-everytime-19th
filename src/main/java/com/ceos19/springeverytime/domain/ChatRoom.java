package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;

public class ChatRoom {
    @Id
    @GeneratedValue
    private Long roomId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User member1;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User member2;
}
