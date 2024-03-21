package com.ceos19.springboot.postlike.repository;

import com.ceos19.springboot.postlike.entity.Postlike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<Postlike, Long> {
}
