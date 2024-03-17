package com.ceos19.springboot.repository;

import com.ceos19.springboot.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {

}
