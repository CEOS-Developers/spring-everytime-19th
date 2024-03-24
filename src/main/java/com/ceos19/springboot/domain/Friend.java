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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User friendUser;

    @Builder
    public Friend(Boolean isAccepted, User friendUser) {
        this.isAccepted = isAccepted;
        this.friendUser = friendUser;
    }
}
