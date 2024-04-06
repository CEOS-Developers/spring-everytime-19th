package com.ceos19.everytime.domain;

import com.ceos19.everytime.exception.CustomException;
import com.ceos19.everytime.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    public static final int MAX_NAME_LENGTH = 20;
    public static final int MAX_DESCRIPTION_LENGTH = 50;

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

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();


    @Builder
    public Board(String boardName, String description, Member boardManager, University university) {
        this.boardName = boardName;
        this.description = description;
        this.boardManager = boardManager;
        this.university = university;
    }

    public void changeBoardName(String boardName) {
        validateBoardName(boardName);
        this.boardName = boardName;
    }

    public void changeDescription(String description) {
        validateDescription(description);
        this.description = description;
    }

    private void validateBoardName(String boardName) {
        if (boardName.isEmpty() || boardName.length() > MAX_NAME_LENGTH)
            throw new CustomException(ErrorCode.INVALID_PARAMETER);
    }

    private void validateDescription(String description) {
        if (description.isEmpty() || description.length() > MAX_DESCRIPTION_LENGTH)
            throw new CustomException(ErrorCode.INVALID_PARAMETER);
    }

}
