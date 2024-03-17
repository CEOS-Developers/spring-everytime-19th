package com.ceos19.everytime.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="MessageBox")
@Getter
@Setter
public class MessageBox {

    @Id
    @Column(name="messagebox_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long messagebox_id;

    @Column(name="message_num")
    private Long message_num;

    @Column(name="created_at", nullable = false)
    private Timestamp created_at;

    @Column(name="updated_at", nullable = false)
    private Timestamp updated_at;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

}
