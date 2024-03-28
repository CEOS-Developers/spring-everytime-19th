package com.ceos19.everytime.message.service;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ceos19.everytime.message.domain.Message;
import com.ceos19.everytime.user.domain.User;
import com.ceos19.everytime.message.dto.request.MessageReadRequestDto;
import com.ceos19.everytime.message.dto.request.MessageRequestDto;
import com.ceos19.everytime.message.dto.response.MessageResponseDto;
import com.ceos19.everytime.message.repository.MessageRepository;
import com.ceos19.everytime.message.service.MessageService;
import com.ceos19.everytime.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MessageService messageService;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        sender = User.builder()
                .nickname("nickname1")
                .build();
        receiver = User.builder()
                .nickname("nickname2")
                .build();
    }

    @Test
    void 메시지를_전송한다() {
        // given
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

    @Test
    void 메시지를_읽는다() {
        // given
        final Message message = new Message("content", sender, receiver);

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(receiver));
        given(messageRepository.findByIdAndReceiver(anyLong(), any()))
                .willReturn(List.of(message));

        final MessageReadRequestDto request = new MessageReadRequestDto(1L, 2L);

        // when
        final List<MessageResponseDto> result = messageService.readMessage(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.get(0).content()).isEqualTo("content");
            softly.assertThat(result.get(0).senderNickname()).isEqualTo("nickname1");
            softly.assertThat(result.get(0).transferTime()).isNotNull();
        });
    }
}
