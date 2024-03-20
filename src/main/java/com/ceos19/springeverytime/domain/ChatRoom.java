package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue
    private Long roomId;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user1_id")
    private User member1;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user2_id")
    private User member2;
}
