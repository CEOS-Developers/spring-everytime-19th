package com.ceos19.springboot.repository;

import com.ceos19.springboot.domain.Post;
import com.ceos19.springboot.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByTitle(String title);
    void deleteByUser(Users user);
}
