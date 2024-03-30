package com.ceos19.everytime.domain.AboutMessage;


import com.ceos19.everytime.domain.AboutUser.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Message {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long messageId;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    @Builder.Default
    private boolean isDeleted=false;

    @Column(nullable = false)
    @Builder.Default
    private Timestamp sendAt=Timestamp.valueOf(LocalDateTime.now());

    @ManyToOne
    @JoinColumn(name="request_user_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name="response_user_id")
    private User user2;

    @ManyToOne
    @JoinColumn(name="messagebox_id")
    private MessageBox messageBox;


}
