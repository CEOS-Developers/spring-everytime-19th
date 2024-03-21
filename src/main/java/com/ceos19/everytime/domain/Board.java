package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String boardName;

    @Column(nullable = false, length = 50)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member boardManager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;


    @Builder
    public Board (String boardName, String description, Member boardManager, University university){
        this.boardName = boardName;
        this.description = description;
        this.boardManager = boardManager;
        this.university = university;
    }

}
