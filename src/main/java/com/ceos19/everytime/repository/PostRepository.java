package com.ceos19.everytime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos19.everytime.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
