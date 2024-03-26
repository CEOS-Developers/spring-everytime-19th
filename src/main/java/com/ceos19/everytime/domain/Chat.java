package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
public class Chat{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm:ss")
    private LocalDateTime sentAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chatting_room_id")
    private ChattingRoom chattingRoom;

    @Builder
    public Chat(String content, User author, ChattingRoom chattingRoom) {
        this.content = content;
        this.author = author;
        this.sentAt = LocalDateTime.now();
        this.chattingRoom = chattingRoom;
    }
}

