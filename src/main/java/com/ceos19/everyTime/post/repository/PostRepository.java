package com.ceos19.everyTime.post.repository;

import com.ceos19.everyTime.post.domain.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post,Long> {


    @Query("select p from Post p where p.community.id=:communityId order by p.createdAt desc ")
    Slice<Post> findByCommunityIdOrderByCreatedAt(@Param("communityId") Long communityId,Pageable pageable);

    @Query("select distinct p from Post p left join fetch p.postImageList join fetch p.member where p.id=:postId")
    Optional<Post>  findPostByPostIdWithFetchMemberAndPostImageList(@Param("postId") Long postId);



    /*
    @Query("select p.writer,m.name from Post p join fetch p.member m where p.community.id=:communityId")
    List<String> findByCommunityName(@Param("communityId") Long communtiyId);*/

}
