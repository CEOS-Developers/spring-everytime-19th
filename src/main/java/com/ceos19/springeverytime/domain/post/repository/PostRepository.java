package com.ceos19.springeverytime.domain.post.repository;

import com.ceos19.springeverytime.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {}
