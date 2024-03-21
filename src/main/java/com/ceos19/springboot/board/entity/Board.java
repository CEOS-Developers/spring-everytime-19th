package com.ceos19.springboot.board.entity;

import com.ceos19.springboot.common.BaseEntity;
import com.ceos19.springboot.post.entity.Post;
import com.ceos19.springboot.school.entity.School;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(name = "board_name", length = 20)
    private String boardName;

    @Column(name = "board_setting", length = 100)
    private String boardSetting;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @OneToMany(mappedBy ="board", fetch = FetchType.LAZY)
    private List<Post> postList = new ArrayList<>();

}
