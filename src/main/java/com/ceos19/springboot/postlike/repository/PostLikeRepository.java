package com.ceos19.springboot.postlike.repository;

import com.ceos19.springboot.post.domain.Post;
import com.ceos19.springboot.postlike.domain.PostLike;
import com.ceos19.springboot.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    void deleteByPost(Post post);
    void deleteByUser(Users user);
}
