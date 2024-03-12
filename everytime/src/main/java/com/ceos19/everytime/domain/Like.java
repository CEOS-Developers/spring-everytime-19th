package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Like {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

}
