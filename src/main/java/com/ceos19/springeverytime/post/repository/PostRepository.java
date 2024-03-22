package com.ceos19.springeverytime.post.repository;

import com.ceos19.springeverytime.post.domain.Post;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findPostById(long postId);

    void deletePostById(long postId);
}
