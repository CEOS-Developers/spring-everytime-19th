package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long id);
    Optional<Post> findByTitle(String title);

}
