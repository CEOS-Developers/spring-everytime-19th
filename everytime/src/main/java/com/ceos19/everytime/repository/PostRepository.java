package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    @Query("select p from Post p " +
            "left join fetch p.user u")
    List<Post> findAll();

    @Query("select p from Post p " +
            "left join fetch p.user u " +
            "where p.id= :postId")
    Optional<Post> findById(@Param("postId")long postId);

    @Query("select p from Post p " +
            "left join fetch p.user u " +
            "where p.title= :title")
    Optional<Post> findByTitle(@Param("title")String title);
}
