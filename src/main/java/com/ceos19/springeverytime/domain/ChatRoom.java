package com.ceos19.springeverytime.domain;

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
    @ManyToOne
    @JoinColumn(name = "user1_id", updatable = false, nullable = false)
    private User member1;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user2_id", updatable = false, nullable = false)
    private User member2;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    /**
     * 채팅 전송
     * */
    public void send(User sender, String message) {
        if (!(sender.equals(this.member1) || sender.equals(this.member2))) {
            throw new IllegalArgumentException("채팅방에 없는 사용자는 메세지를 보낼 수 없습니다.");
        }
        ChatMessage chatMessage = ChatMessage.builder()
                .room(this)
                .content(message)
                .sender(sender).build();

        this.chatMessages.add(chatMessage);
    }
}
