package com.ceos19.everytime.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.dto.request.MessageRequestDto;
import com.ceos19.everytime.repository.MessageRepository;
import com.ceos19.everytime.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MessageService messageService;

    @Test
    void 메시지를_전송한다() {
        // given
        final User sender = User.builder()
                .nickname("nickname1")
                .build();
        final User receiver = User.builder()
                .nickname("nickname2")
                .build();

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(sender));
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(receiver));

        final MessageRequestDto request = new MessageRequestDto(1L, 2L, "content");

        // when
        messageService.sendMessage(request);

        // then
        verify(messageRepository).save(any());
    }
}
