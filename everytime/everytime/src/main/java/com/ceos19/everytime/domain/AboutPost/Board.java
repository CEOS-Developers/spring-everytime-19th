package com.ceos19.everytime.domain.AboutPost;

import com.ceos19.everytime.domain.AboutPost.Post;
import com.ceos19.everytime.domain.AboutUser.School;
import com.ceos19.everytime.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Board extends BaseTimeEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long boardId;

    @Column(nullable = false)
    private String boardName;

    @Column(nullable = false)
    @Builder.Default
    private boolean isAnonymity=true;

    @Column(nullable = false)
    @Builder.Default
    private Long postNum=0L;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="school_id")
    private School school;

}
