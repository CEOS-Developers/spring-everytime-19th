package com.ceos19.springboot.board.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(name = "board_name", length = 20)
    private String boardName;

    @Column(name = "board_setting", length = 100)
    private String boardSetting;

}
