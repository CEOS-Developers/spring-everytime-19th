package com.ceos19.everytime.domain.AboutMessage;


import com.ceos19.everytime.domain.AboutUser.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Message {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(nullable = false)
    private Timestamp sendAt = Timestamp.valueOf(LocalDateTime.now());

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_user_id")
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_user_id")
    private User user2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "messagebox_id")
    private MessageBox messageBox;

    @Builder
    public Message(Long messageId, String contents, boolean isDeleted, Timestamp sendAt, User user1, User user2, MessageBox messageBox) {
        this.messageId = messageId;
        this.contents = contents;
        this.isDeleted = isDeleted;
        this.sendAt = sendAt;
        this.user1 = user1;
        this.user2 = user2;
        this.messageBox = messageBox;
    }
}
