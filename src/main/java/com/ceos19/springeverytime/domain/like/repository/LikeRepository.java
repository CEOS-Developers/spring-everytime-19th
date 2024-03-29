package com.ceos19.springeverytime.domain.like.repository;

import com.ceos19.springeverytime.domain.comment.domain.Comment;
import com.ceos19.springeverytime.domain.post.domain.Post;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.like.domain.CommentLike;
import com.ceos19.springeverytime.domain.like.domain.Like;
import com.ceos19.springeverytime.domain.like.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    @Query("select l from PostLike l where l.post = :post and l.user = :user")
    Optional<PostLike> findPostLikeByPostAndUser(@Param("post")Post post, @Param("user")User user);

    @Query("select l from CommentLike l where l.comment = :comment and l.user = :user")
    Optional<CommentLike> findCommentLikeByCommentAndUser(@Param("comment") Comment comment, @Param("user")User user);
}
