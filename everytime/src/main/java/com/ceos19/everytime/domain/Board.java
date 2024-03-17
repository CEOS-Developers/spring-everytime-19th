package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Board")
@Getter
@Setter
public class Board {

    @Id
    @Column(name="board_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long board_id;

    @Column(name="board_name", nullable = false)
    private String board_name;

    @Column(name="is_anonymity", nullable = false)
    private boolean is_anonymity;

    @Column(name="post_num", nullable = false)
    private Long post_num;

    @Column(name="created_at", nullable = false)
    private Timestamp created_at;

    @Column(name="updated_at", nullable = false)
    private Timestamp updated_at;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="school_id")
    private School school;

    @OneToMany(mappedBy = "board")
    List<Post> posts = new ArrayList<Post>();
}
