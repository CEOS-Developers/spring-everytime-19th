package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue
    private Long messageId;

    @NonNull
    @Column(length = 2000, nullable = false)
    private String content;

    @NonNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender;
}
