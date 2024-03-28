package com.ceos19.springboot.repository;

import com.ceos19.springboot.domain.Post;
import com.ceos19.springboot.domain.PostLike;
import com.ceos19.springboot.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    void deleteByPost(Post post);
    void deleteByUser(Users user);
}
