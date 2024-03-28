package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @EntityGraph(attributePaths = {"post"})
    List<Comment> findByCommenterId(Long commenterId);

    @EntityGraph(attributePaths = {"commenter"})
    List<Comment> findByPostId(Long postId);

    @EntityGraph(attributePaths = {"commenter"})
    List<Comment> findByParentCommentId(Long parentCommentId);

    void deleteAllByPostId(Long postId);

    void deleteAllByCommenterId(Long commenterId);
}
