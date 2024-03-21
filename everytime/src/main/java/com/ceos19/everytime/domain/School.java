package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="School")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public class School {
    @Id
    @Column(name="school_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long school_id;

    @Column(name="school_name", nullable = false)
    private String school_name;

    @Column(name="created_at", nullable = false)
    private Timestamp created_at;

    @Column(name="updated_at", nullable = false)
    private Timestamp updated_at;

    @Column(name="student_num", nullable = false)
    private Long student_num;

    @OneToMany(mappedBy = "school")
    List<User> users = new ArrayList<User>();

    @OneToMany(mappedBy = "school")
    List<Board> boards = new ArrayList<Board>();

    public School(String school_name, Timestamp created_at, Timestamp updated_at, Long student_num){
        this.school_name = school_name;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.student_num = student_num;
    }
}
