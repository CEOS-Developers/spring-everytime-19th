package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {}
