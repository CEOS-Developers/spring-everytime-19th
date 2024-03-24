package com.ceos19.everytime.postlike.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos19.everytime.post.domain.Post;
import com.ceos19.everytime.postlike.domain.PostLike;
import com.ceos19.everytime.user.domain.User;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByPostAndUser(final Post post, final User user);

    Optional<PostLike> findByPostAndUser(final Post post, final User user);
}
