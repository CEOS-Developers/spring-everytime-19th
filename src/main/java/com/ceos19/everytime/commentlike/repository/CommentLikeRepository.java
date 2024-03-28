package com.ceos19.everytime.commentlike.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos19.everytime.comment.domain.Comment;
import com.ceos19.everytime.commentlike.domain.CommentLike;
import com.ceos19.everytime.user.domain.User;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByCommentAndUser(final Comment comment, final User user);

    Optional<CommentLike> findByCommentAndUser(final Comment comment, final User user);

    void deleteAllByComment(final Comment comment);
}
