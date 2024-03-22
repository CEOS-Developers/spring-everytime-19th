package com.ceos19.springeverytime.message.repository;

import com.ceos19.springeverytime.message.domain.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findMessagesBySender_Id(Long userId);

    List<Message> findMessagesByRoom_Id(Long roomId);
}
