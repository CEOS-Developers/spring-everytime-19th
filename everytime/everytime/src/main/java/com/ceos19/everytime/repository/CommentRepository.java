package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.AboutPost.Comment;
import com.ceos19.everytime.domain.AboutUser.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    Optional<Comment> findByCommentId(long commentId);

    List<Comment> findByPostId(long postId);
}
