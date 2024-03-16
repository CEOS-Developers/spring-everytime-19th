package com.ceos19.springboot.post.repository;

import com.ceos19.springboot.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
