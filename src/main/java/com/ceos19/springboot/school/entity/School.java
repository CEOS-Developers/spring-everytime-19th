package com.ceos19.springboot.school.entity;

import com.ceos19.springboot.board.entity.Board;
import com.ceos19.springboot.common.BaseEntity;
import com.ceos19.springboot.user.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "school")
public class School extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schoolId;

    private String schoolName;

    @OneToOne(mappedBy = "school",fetch = FetchType.LAZY)
    private Board board;

    @OneToOne(mappedBy = "school",fetch = FetchType.LAZY)
    private User user;

}
