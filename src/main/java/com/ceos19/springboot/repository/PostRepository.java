package com.ceos19.springboot.repository;

import com.ceos19.springboot.domain.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Posts, UUID> {
    Posts findByTitle(String title);
}
