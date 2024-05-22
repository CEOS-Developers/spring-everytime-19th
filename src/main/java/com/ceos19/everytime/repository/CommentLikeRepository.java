package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentIdAndMemberId(Long commentId, Long memberId);
    Boolean existsByCommentIdAndMemberId(Long commentId, Long memberId);
}
