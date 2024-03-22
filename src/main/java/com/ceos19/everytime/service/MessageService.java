package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.Message;
import com.ceos19.everytime.repository.MemberRepository;
import com.ceos19.everytime.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MessageService {
    public static final int MAX_CONTENT_LENGTH = 2000;

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    public Message create(Long senderId, Long receiverId, String content){
        Optional<Member> sender = memberRepository.findById(senderId);
        Optional<Member> receiver = memberRepository.findById(receiverId);

        if(sender.isEmpty() || receiver.isEmpty() || !validateContent(content)){
            log.info("[Service][createMessage] FAIL");
            return null;
        }

        Message message = new Message(sender.get(), receiver.get(), content);
        messageRepository.save(message);
        log.info("[Service][createMessage] SUCCESS");
        return message;
    }

    public void updateReadStatus(Long receiverId, Long messageId){
        Optional<Message> message = messageRepository.findById(messageId);
        Optional<Member> receiver = memberRepository.findById(receiverId);
        if(message.isEmpty() || receiver.isEmpty()){
            log.info("[Service][updateReadStatus] FAIL");
        }
        else{
            message.get().updateReadStatus();
            log.info("[Service][updateReadStatus] SUCCESS");
        }

    }


    private boolean validateContent(String content){
        if(content.isEmpty() || content.length()> MAX_CONTENT_LENGTH)
            return false;
        return true;
    }

}
