package com.ceos19.springeverytime.domain.chatmessage.domain;

import com.ceos19.springeverytime.domain.BaseEntity;
import com.ceos19.springeverytime.domain.chatroom.domain.ChatRoom;
import com.ceos19.springeverytime.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

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
