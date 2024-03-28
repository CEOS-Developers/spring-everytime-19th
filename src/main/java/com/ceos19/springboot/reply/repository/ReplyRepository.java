package com.ceos19.springboot.reply.repository;

import com.ceos19.springboot.comment.entity.Comment;
import com.ceos19.springboot.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    @Query("select r from Reply r where r.comment =:comment")
    List<Reply> findAllByComment(Comment comment);
}
