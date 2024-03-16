package com.ceos19.springboot.comment.entity;

import com.ceos19.springboot.common.BaseEntity;
import com.ceos19.springboot.reply.entity.Reply;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String content;

    private Integer contentLike;

    @OneToMany(mappedBy = "reply", cascade = CascadeType.REMOVE)
    private List<Reply> replyList = new ArrayList<>();

}
