package com.ceos19.springeverytime.postlike.repository;

import com.ceos19.springeverytime.postlike.domain.PostLike;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {

    List<PostLike> findByPostId(Long id);

    Optional<PostLike> findByUser_IdAndPost_Id(Long userId, Long postId);

}
