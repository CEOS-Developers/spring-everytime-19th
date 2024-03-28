package com.ceos19.springboot.postlike.repository;

import com.ceos19.springboot.comment.entity.Comment;
import com.ceos19.springboot.commentlike.entity.CommentLike;
import com.ceos19.springboot.post.entity.Post;
import com.ceos19.springboot.postlike.entity.postLike;
import com.ceos19.springboot.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<postLike, Long> {

    @Query("SELECT p FROM postLike p WHERE p.user = :user and p.post = :post")
    Optional<postLike> findByUserAndComment(User user, Post post);

}
