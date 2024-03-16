package com.ceos19.springeverytime.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

public class ChatRoom {
    @Id
    private Long id;

    @OneToOne
    private User member1;

    @OneToOne
    private User member2;
}
