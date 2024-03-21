package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.like.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {}
