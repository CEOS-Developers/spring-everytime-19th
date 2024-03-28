package com.ceos19.everytime.message.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos19.everytime.message.domain.Message;
import com.ceos19.everytime.user.domain.User;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByIdAndReceiver(final Long messageId, final User receiver);
}
