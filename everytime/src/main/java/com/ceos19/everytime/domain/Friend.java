package com.ceos19.everytime.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="Friend")
@Getter
@Setter
public class Friend {
    @Id
    @Column(name="friend_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long friend_id;

    @Column(name="is_accepted", nullable = false)
    private boolean is_accepted;

    @ManyToOne
    @JoinColumn(name="request_user_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name="response_user_id")
    private User user2;

}
