package com.ceos19.springboot.repository;

import com.ceos19.springboot.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

}
