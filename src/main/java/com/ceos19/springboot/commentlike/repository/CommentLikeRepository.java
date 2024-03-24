package com.ceos19.springboot.commentlike.repository;

import com.ceos19.springboot.comment.entity.Comment;
import com.ceos19.springboot.commentlike.entity.CommentLike;
import com.ceos19.springboot.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    @Query("SELECT c FROM CommentLike c WHERE c.user = :user and c.comment = :comment")
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);

}
