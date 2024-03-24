package com.ceos19.everyTime.post.repository;

import com.ceos19.everyTime.post.domain.Reply;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

    boolean existsById(Long id);

    @Query("select r from Reply r where r.post.id=:postId and r.parent=null ")
    List<Reply> findParentReplyByPostId(@Param("postId") Long postId);

    @Query("select distinct r from Reply r  left join fetch r.childList join fetch r.member where r.post.id=:postId and r.parent=null")
    List<Reply> findParentReplyByPostIdWithFetchMember(@Param("postId")Long postId);

    @Query("select r from Reply r  where r.parent.id=:parentId")
    List<Reply> findChildByParentId(@Param("parentId") Long parentId);

    @Query("select r from Reply r where r.member.id=:memberId and r.post.id=:postId and r.isHideNickName=true order by r.id limit 1")
    Optional<Reply> findDefaultNickNameReplyByMemberIdAndPostIdLimitOne(@Param("memberId")Long memberId,@Param("postId") Long postId);

    boolean existsByParentId(Long parentId);

}
