package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
@ToString(exclude = {"sender", "receiver"})
public class Message {  // 쪽지, message는 user의 비즈니스 연관관계 편의 메서드로 저장함. 별도의 리포지토리 없다.
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Column(nullable = false)
    private String title;
    private String content;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @ManyToOne(fetch = LAZY)
    @Setter(value = PROTECTED)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = LAZY)
    @Setter(value = PROTECTED)
    @JoinColumn(name = "receiver_id")
    private User receiver;
    
    public Message(String title, String content) {
        this.title = title;
        this.content = content;
        this.sentAt = LocalDateTime.now();
    }

    public void setSenderAndReceiver(User sender, User receiver) {
        sender.addSendMessage(this);
        receiver.addReceiveMessage(this);
    }
}
