package com.ceos19.springeverytime.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

public class ChatRoom {
    @Id
    private Long id;

    @ManyToOne
    private User member1;

    @ManyToOne
    private User member2;
}
