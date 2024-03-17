package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
