package com.ceos19.everytime.domain.AboutUser;

import com.ceos19.everytime.domain.AboutPost.Board;
import com.ceos19.everytime.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class School extends BaseTimeEntity {
    @Id
    @Column(name="school_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long schoolId;

    @Column(name="school_name", nullable = false)
    private String schoolName;

    @Column(name="student_num", nullable = false)
    @Builder.Default
    private Long studentNum=0L;

    @OneToMany(mappedBy = "school",fetch = FetchType.LAZY)
    @Builder.Default
    List<User> users = new ArrayList<User>();

    @OneToMany(mappedBy = "school",fetch = FetchType.LAZY)
    @Builder.Default
    List<Board> boards = new ArrayList<Board>();

    public School(String schoolName, Long studentNum){
        this.schoolName = schoolName;
        this.studentNum = studentNum;
    }
}
