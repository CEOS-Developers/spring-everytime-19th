package com.ceos19.springboot.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private Boolean isAccepted;

    @Column(nullable = false)
    private Long myId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "friend_id")
    private User friendUser;

    @Builder
    public Friend(Boolean isAccepted, Long myId, User friendUser) {
        this.isAccepted = isAccepted;
        this.myId = myId;
        this.friendUser = friendUser;
    }
}
