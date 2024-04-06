package com.ceos19.springeverytime.domain.like.domain;

import com.ceos19.springeverytime.domain.BaseEntity;
import com.ceos19.springeverytime.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "likes")
@DiscriminatorColumn(name = "dtype")
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
public abstract class Like extends BaseEntity {
    @Id
    @GeneratedValue
    private Long likeId;

    @NonNull
    @ManyToOne
    private User user;
}
