package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.Message;
import com.ceos19.everytime.dto.CreateMessageRequest;
import com.ceos19.everytime.dto.DeleteRequest;
import com.ceos19.everytime.dto.MessageResponse;
import com.ceos19.everytime.exception.CustomException;
import com.ceos19.everytime.repository.MemberRepository;
import com.ceos19.everytime.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ceos19.everytime.exception.ErrorCode.DATA_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MessageService {
    public static final int MAX_CONTENT_LENGTH = 2000;

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    public Long createMessage (CreateMessageRequest createMessageRequest){
        final Member sender = memberRepository.findById(createMessageRequest.getSenderId())
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));
        final Member receiver = memberRepository.findById(createMessageRequest.getReceiverId())
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));

        Message message = new Message(sender, receiver, createMessageRequest.getContent());
        return messageRepository.save(message)
                .getId();
    }

    public List<MessageResponse> findEveryMessage(Long memberId){
        final Member sender = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));

        List<MessageResponse> messageResponseList = new ArrayList<>();
        for(Message message : messageRepository.findBySenderId(memberId)){
            messageResponseList.add(MessageResponse.from(message));
        }

        return messageResponseList;
    }

    public void deleteMessage (Long messageId, DeleteRequest deleteRequest){

        final Member member = memberRepository.findById(deleteRequest.getMemberId())
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));
        final Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));

        if(messageRepository.existsByIdAndSenderId(message.getId(), member.getId()) || messageRepository.existsByIdAndReceiverId(message.getId(), member.getId())){
            messageRepository.deleteById(messageId);
        }

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

}
