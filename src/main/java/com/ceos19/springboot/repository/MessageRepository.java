package com.ceos19.springboot.repository;

import com.ceos19.springboot.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message, Long> {
}
