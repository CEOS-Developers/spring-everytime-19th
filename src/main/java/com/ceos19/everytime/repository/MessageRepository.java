package com.ceos19.everytime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos19.everytime.domain.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
