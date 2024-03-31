package com.ceos19.springboot.postlike.repository;

import com.ceos19.springboot.post.domain.Post;
import com.ceos19.springboot.postlike.domain.PostLike;
import com.ceos19.springboot.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    void deleteByPost(Post post);
    void deleteByUser(Users user);
    @Query("SELECT pl FROM PostLike pl WHERE pl.user.userId = :userId AND pl.post.postId = :postId AND pl.deleted = false")
    Optional<PostLike> findByUserIdAndPostId(Long userId, Long postId);
}
