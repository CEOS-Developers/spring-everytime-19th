package com.ceos19.everytime.post.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.ceos19.everytime.board.dto.request.BoardPostsRequestDto;
import com.ceos19.everytime.board.dto.response.BoardPostsResponseDto;
import com.ceos19.everytime.config.SecurityConfig;
import com.ceos19.everytime.post.dto.request.PostCreateRequestDto;
import com.ceos19.everytime.post.dto.response.PostResponseDto;
import com.ceos19.everytime.post.service.PostService;
import com.ceos19.everytime.postlike.dto.request.PostLikeRequestDto;
import com.ceos19.everytime.postlike.service.PostLikeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(value = PostController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
@WithMockUser
class PostControllerTest {

    private static final String DEFAULT_POST_URL = "/api/posts";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @MockBean
    private PostLikeService postLikeService;

    @Test
    void 게시글_작성에_성공한다() throws Exception {
        // given
        final PostCreateRequestDto request = PostCreateRequestDto.builder()
                .title("title")
                .content("content")
                .isAnonymous(true)
                .boardId(1L)
                .build();

        doNothing().when(postService).createPost(any(), anyString());

        // when & then
        mockMvc.perform(post(DEFAULT_POST_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void 게시판에_해당하는_모든_게시글을_조회한다() throws Exception {
        // given
        final BoardPostsRequestDto request = new BoardPostsRequestDto(1L);

        given(postService.getPosts(any()))
                .willReturn(List.of(new BoardPostsResponseDto("title1", "writerNickname1"),
                        new BoardPostsResponseDto("title2", "writerNickname2")));

        // when & then
        mockMvc.perform(get(DEFAULT_POST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpectAll(
                        jsonPath("$[0].title").value("title1"),
                        jsonPath("$[0].writerNickname").value("writerNickname1"),
                        jsonPath("$[1].title").value("title2"),
                        jsonPath("$[1].writerNickname").value("writerNickname2")
                );
    }

    @Test
    void 게시글을_조회한다() throws Exception {
        // given
        given(postService.getPost(any()))
                .willReturn(new PostResponseDto("title", "content", "nickname", "자유게시판", List.of()));

        // when & then
        mockMvc.perform(get(DEFAULT_POST_URL + "/{postId}", 1L))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.username").value("nickname"))
                .andExpect(jsonPath("$.boardName").value("자유게시판"))
                .andExpect(jsonPath("$.comments").isEmpty());
    }

    @Test
    void 게시글_좋아요() throws Exception {
        // given
        doNothing().when(postLikeService).like(anyLong(), any());

        final Long postId = 1L;
        final PostLikeRequestDto request = new PostLikeRequestDto(1L);

        // when & then
        mockMvc.perform(post(DEFAULT_POST_URL + "/likes/{postId}", postId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void 게시글에_좋아요를_취소한다() throws Exception {
        // given
        doNothing().when(postLikeService).cancelLike(anyLong(), any());

        final Long postId = 1L;
        final PostLikeRequestDto request = new PostLikeRequestDto(1L);

        // when & then
        mockMvc.perform(delete(DEFAULT_POST_URL + "/likes/{postId}", postId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
