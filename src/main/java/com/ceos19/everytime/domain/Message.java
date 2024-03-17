package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column
    private boolean isRead;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Message (Member sender, Member receiver, String content, LocalDateTime createdAt){
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.isRead = false;
        this.createdAt = createdAt;
    }

}
