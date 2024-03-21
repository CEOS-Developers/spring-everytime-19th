package com.ceos19.springeverytime.domain.like;

import com.ceos19.springeverytime.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "likes")
@DiscriminatorColumn(name = "dtype")
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
public abstract class Like {
    @Id
    @GeneratedValue
    private Long likeId;

    @NonNull
    @ManyToOne
    private User user;

    @NonNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
}
