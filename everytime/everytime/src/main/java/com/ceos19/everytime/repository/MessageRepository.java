package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.AboutMessage.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MessageRepository extends JpaRepository<Message,Long> {

}
