package com.ceos19.springboot.reply.repository;

import com.ceos19.springboot.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
