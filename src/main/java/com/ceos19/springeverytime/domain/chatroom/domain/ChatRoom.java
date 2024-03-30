package com.ceos19.springeverytime.domain.chatroom.domain;

import com.ceos19.springeverytime.domain.BaseEntity;
import com.ceos19.springeverytime.domain.chatmessage.domain.ChatMessage;
import com.ceos19.springeverytime.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Getter
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue
    private Long roomId;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id", updatable = false, nullable = false)
    private User member1;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id", updatable = false, nullable = false)
    private User member2;

    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
    private List<ChatMessage> chatMessages = new ArrayList<>();
}
