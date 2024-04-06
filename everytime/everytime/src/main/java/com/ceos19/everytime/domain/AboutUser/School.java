package com.ceos19.everytime.domain.AboutUser;

import com.ceos19.everytime.domain.AboutPost.Board;
import com.ceos19.everytime.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class School extends BaseTimeEntity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schoolId;

    @Column( nullable = false)
    private String schoolName;

    @Column(nullable = false)
    private Long studentNum = 0L;

    @OneToMany(mappedBy = "school")
    List<User> users = new ArrayList<User>();

    @OneToMany(mappedBy = "school")
    List<Board> boards = new ArrayList<Board>();

    public School(String schoolName, Long studentNum){
        this.schoolName = schoolName;
        this.studentNum = studentNum;
    }

    @Builder
    public School(Long schoolId, String schoolName, Long studentNum) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.studentNum = studentNum;
    }
}
