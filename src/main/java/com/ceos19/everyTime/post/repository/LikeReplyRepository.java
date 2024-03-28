package com.ceos19.everyTime.post.repository;

import com.ceos19.everyTime.post.domain.LikeReply;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeReplyRepository extends JpaRepository<LikeReply,Long> {

    Optional<LikeReply> findByMemberIdAndReplyId(Long memberId,Long replyId);

}
