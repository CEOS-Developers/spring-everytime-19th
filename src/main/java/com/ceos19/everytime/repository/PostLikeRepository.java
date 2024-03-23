package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.PostLike;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
    @EntityGraph(attributePaths = {"user", "post"})
    List<PostLike> findByPostId(Long postId);

    @EntityGraph(attributePaths = {"user", "post"})
    Optional<PostLike> findByPostIdAndUserId(Long postId,Long userId);

    void deleteAllByPostId(Long postId);
}
