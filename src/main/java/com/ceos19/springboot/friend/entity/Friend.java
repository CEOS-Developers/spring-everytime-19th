package com.ceos19.springboot.friend.entity;

import com.ceos19.springboot.common.BaseEntity;
import com.ceos19.springboot.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "friend")
public class Friend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users1")
    private User user1;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users2")
    private User user2;

    @ColumnDefault("false")
    @Column(name = "user_status")
    private Boolean userStatus;
}
