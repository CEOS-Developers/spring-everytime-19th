package com.ceos19.springboot.message.domain;

import com.ceos19.springboot.users.domain.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE Message SET deleted = true WHERE message_id = ?")
@Where(clause = "deleted = false")
public class Message {
    @Id
    @GeneratedValue
    private Long messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Users sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Users receiver;

    private String content;

    private final boolean deleted = false;

    public Message(Users sender,
                   Users receiver,
                   String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }
}
