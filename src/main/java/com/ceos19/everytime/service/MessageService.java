package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.Message;
import com.ceos19.everytime.dto.message.CreateMessageRequest;
import com.ceos19.everytime.dto.message.MessageResponse;
import com.ceos19.everytime.exception.CustomException;
import com.ceos19.everytime.repository.MemberRepository;
import com.ceos19.everytime.repository.MessageRepository;
import com.ceos19.everytime.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.ceos19.everytime.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createMessage (CreateMessageRequest createMessageRequest, CustomUserDetails userDetails){

        //final Member sender = memberRepository.findById(senderId)
        final Member sender = userDetails.getMember();
        final Member receiver = memberRepository.findById(createMessageRequest.getReceiverId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(createMessageRequest.getContent())
                .build();

        return messageRepository.save(message)
                .getId();
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> findEveryMessage(CustomUserDetails userDetails){

        final Member member = userDetails.getMember();;

        return messageRepository.findBySenderId(sender.getId())
                .stream().map(MessageResponse::from).toList();
    }

    @Transactional
    public void deleteMessage (CustomUserDetails userDetails, Long messageId){

        final Member member = userDetails.getMember();;
        final Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(MESSAGE_NOT_FOUND));

        if(messageRepository.existsByIdAndSenderId(message.getId(), member.getId()) ||
                messageRepository.existsByIdAndReceiverId(message.getId(), member.getId())){
            messageRepository.deleteById(messageId);
        }
    }

    @Transactional
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

}
