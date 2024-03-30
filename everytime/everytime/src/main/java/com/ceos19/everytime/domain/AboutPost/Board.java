package com.ceos19.everytime.domain.AboutPost;


import com.ceos19.everytime.domain.AboutUser.School;
import com.ceos19.everytime.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Board extends BaseTimeEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(nullable = false)
    private String boardName;

    @Column(nullable = false)
    private boolean isAnonymity = true;

    @Column(nullable = false)
    private Long postNum = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @Builder
    public Board(Long boardId, String boardName, boolean isAnonymity, Long postNum, School school) {
        this.boardId = boardId;
        this.boardName = boardName;
        this.isAnonymity = isAnonymity;
        this.postNum = postNum;
        this.school = school;
    }
}
