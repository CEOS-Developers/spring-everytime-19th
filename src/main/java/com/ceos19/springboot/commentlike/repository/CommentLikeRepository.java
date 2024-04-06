package com.ceos19.springboot.commentlike.repository;

import com.ceos19.springboot.comment.domain.Comment;
import com.ceos19.springboot.commentlike.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    @Query("SELECT cl FROM CommentLike cl WHERE cl.user.userId = :userId AND cl.comment.commentId = :commentId")
    Optional<CommentLike> findByUserIdAndCommentId(Long userId, Long commentId);

    Optional<CommentLike> findByComment(Comment comment);
}
