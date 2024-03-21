package com.ceos19.everyTime.post.repository;

import com.ceos19.everyTime.post.domain.Post;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post,Long> {


    @Query("select p from Post p where p.community.id=:communityId order by p.createdAt desc ")
    List<Post> findByCommunityIdOrderByCreatedAt(@Param("communityId") Long communityId);

}
