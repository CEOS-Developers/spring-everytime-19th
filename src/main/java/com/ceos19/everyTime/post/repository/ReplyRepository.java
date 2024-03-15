package com.ceos19.everyTime.post.repository;

import com.ceos19.everyTime.post.domain.Reply;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

}
