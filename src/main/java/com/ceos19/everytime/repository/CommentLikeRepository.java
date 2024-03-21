package com.ceos19.everytime.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos19.everytime.domain.Comment;
import com.ceos19.everytime.domain.CommentLike;
import com.ceos19.everytime.domain.User;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByCommentAndUser(final Comment comment, final User user);

    Optional<CommentLike> findByCommentAndUser(final Comment comment, final User user);
}
