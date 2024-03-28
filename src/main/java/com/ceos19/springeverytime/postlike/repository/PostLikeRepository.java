package com.ceos19.springeverytime.postlike.repository;

import com.ceos19.springeverytime.postlike.domain.PostLike;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {

    List<PostLike> findByPostId(Long id);

    List<PostLike> findByUserId(Long id);
}
