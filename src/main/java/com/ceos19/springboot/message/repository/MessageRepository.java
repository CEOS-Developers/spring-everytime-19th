package com.ceos19.springboot.message.repository;

import com.ceos19.springboot.message.domain.Message;
import com.ceos19.springboot.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<List<Message>> findBySender(Users sender);
}
