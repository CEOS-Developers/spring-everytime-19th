package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.Message;
import com.ceos19.everytime.dto.CreateMessageRequest;
import com.ceos19.everytime.dto.MessageResponse;
import com.ceos19.everytime.exception.CustomException;
import com.ceos19.everytime.repository.MemberRepository;
import com.ceos19.everytime.repository.MessageRepository;
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
    public Long createMessage (CreateMessageRequest createMessageRequest, Long senderId){

        final Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
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
    public List<MessageResponse> findEveryMessage(Long memberId){
        final Member sender = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        return messageRepository.findBySenderId(sender.getId())
                .stream().map(MessageResponse::from).toList();
    }

    @Transactional
    public void deleteMessage (Long memberId, Long messageId){

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        final Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(MESSAGE_NOT_FOUND));

        if(messageRepository.existsByIdAndSenderId(message.getId(), member.getId()) || messageRepository.existsByIdAndReceiverId(message.getId(), member.getId())){
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
