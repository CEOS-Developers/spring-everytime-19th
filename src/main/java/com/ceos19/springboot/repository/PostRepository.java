package com.ceos19.springboot.repository;

import com.ceos19.springboot.domain.Post;
import com.ceos19.springboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAll();

    Optional<Post> findById(@Param("postId")long postId);

    Optional<Post> findByTitle(@Param("title")String title);
}