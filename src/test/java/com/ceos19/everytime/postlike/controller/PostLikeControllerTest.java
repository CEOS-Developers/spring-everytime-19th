package com.ceos19.everytime.postlike.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.ceos19.everytime.postlike.dto.request.PostLikeRequestDto;
import com.ceos19.everytime.postlike.service.PostLikeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PostLikeController.class)
class PostLikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostLikeService postLikeService;

    @Test
    void 게시글에_좋아요를_누른다() throws Exception {
        // given
        final PostLikeRequestDto request = new PostLikeRequestDto(1L, 1L);

        doNothing().when(postLikeService).like(any());

        // when & then
        mockMvc.perform(post("/api/postlikes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
