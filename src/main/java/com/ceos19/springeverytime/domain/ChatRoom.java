package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;

public class ChatRoom {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User member1;

    @ManyToOne
    private User member2;
}
