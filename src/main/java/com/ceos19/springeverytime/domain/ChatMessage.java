package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue
    private Long messageId;

    @NonNull
    @Column(length = 2000, nullable = false)
    private String content;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender;

    @Builder
    public ChatMessage(@NonNull String content, @NonNull ChatRoom room, @NonNull User sender) {
        this.content = content;
        this.room = room;
        this.sender = sender;
    }
}
