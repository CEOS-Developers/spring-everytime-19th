package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Comment;
import com.ceos19.everytime.domain.CommentLike;
import com.ceos19.everytime.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentIdAndUser(Comment commentId, Member user);
}
