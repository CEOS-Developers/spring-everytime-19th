package com.ceos19.everytime.message.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.ceos19.everytime.config.SecurityConfig;
import com.ceos19.everytime.message.dto.request.MessageReadRequestDto;
import com.ceos19.everytime.message.dto.request.MessageRequestDto;
import com.ceos19.everytime.message.dto.response.MessageResponseDto;
import com.ceos19.everytime.message.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(value = MessageController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
@WithMockUser
class MessageControllerTest {

    private static final String DEFAULT_MESSAGE_URL = "/api/messages";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageService messageService;

    @Test
    void 쪽지를_전송한다() throws Exception {
        // given
        final MessageRequestDto request = new MessageRequestDto(1L, 2L, "content");

        doNothing().when(messageService).sendMessage(request);

        // when & then
        mockMvc.perform(post(DEFAULT_MESSAGE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void 쪽지를_읽는다() throws Exception {
        // given
        final MessageReadRequestDto request = new MessageReadRequestDto(1L, 2L);

        given(messageService.readMessage(any()))
                .willReturn(List.of(new MessageResponseDto("sender", "content", LocalDateTime.now())));

        // when & then
        mockMvc.perform(get(DEFAULT_MESSAGE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpectAll(
                        jsonPath("$[0].senderNickname").value("sender"),
                        jsonPath("$[0].content").value("content"),
                        jsonPath("$[0].transferTime").exists()
                );
    }
}
