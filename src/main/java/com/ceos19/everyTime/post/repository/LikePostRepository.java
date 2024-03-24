package com.ceos19.everyTime.post.repository;

import com.ceos19.everyTime.post.domain.LikePost;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikePostRepository extends JpaRepository<LikePost,Long> {

    /*@Query("select count(lp) from LikePost lp where lp.post.id=:postId")
    Long countByPostId(@Param(("postId")) Long postId);
*/

    Optional<LikePost> findByMemberIdAndPostId(Long memberId,Long postId);




}
