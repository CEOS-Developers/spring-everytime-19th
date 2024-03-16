package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @Column(length = 100, nullable = false)
    private String title;

    @NonNull
    @Column(length = 2000, nullable = false)
    private String content;

    @NonNull
    private boolean isAnonymous;

    @NonNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @NonNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;

    @NonNull
    @ManyToOne
    private User author;

    @NonNull
    @ManyToOne
    private Category category;

}
