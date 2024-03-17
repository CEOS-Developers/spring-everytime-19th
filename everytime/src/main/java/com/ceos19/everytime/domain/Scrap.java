package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="Scrap")
@Getter
@Setter
public class Scrap {
    @Id
    @Column(name="scrap_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long scrap_id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
}
