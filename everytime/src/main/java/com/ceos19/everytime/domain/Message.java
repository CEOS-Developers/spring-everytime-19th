package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Message {  //쪽지
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "message_id")
    private Long id;

    private String title;
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;


}
