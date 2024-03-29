package com.ceos19.springeverytime.domain.like.repository;

import com.ceos19.springeverytime.domain.comment.domain.Comment;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.like.domain.CommentLike;
import com.ceos19.springeverytime.domain.like.domain.Like;
import com.ceos19.springeverytime.domain.like.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    @Query("select l from PostLike l where l.post.postId = :postId and l.user.userId = :userId")
    Optional<PostLike> findPostLikeByPostIdAndUserId(@Param("postId")Long postId, @Param("userId")Long userId);

    @Query("select l from CommentLike l where l.comment.commentId = :commentId and l.user.userId = :userId")
    Optional<CommentLike> findCommentLikeByCommentIdAndUserId(@Param("commentId") Long commentId, @Param("userId")Long userId);
}
