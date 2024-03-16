package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;

public class ChatRoom {
    @Id
    @GeneratedValue
    private Long roomId;

    @ManyToOne
    private User member1;

    @ManyToOne
    private User member2;
}
