package com.ceos19.everytime.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name="Message")
@Getter
@Setter
public class Message {
    @Id
    @Column(name="message_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long message_id;

    @Column(name="contents", nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name="request_user_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name="response_user_id")
    private User user2;

    @ManyToOne
    @JoinColumn(name="messagebox_id")
    private MessageBox messageBox;

    @Column(name="is_deleted", nullable = false)
    private boolean is_deleted;

    @Column(name="send_at")
    private Timestamp send_at;
}
