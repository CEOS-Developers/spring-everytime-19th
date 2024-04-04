package com.ceos19.everytime.user.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.ceos19.everytime.global.exception.BadRequestException;
import com.ceos19.everytime.global.exception.ExceptionCode;
import com.ceos19.everytime.user.dto.request.UserSaveRequestDto;
import com.ceos19.everytime.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private static final String USER_DEFAULT_URL = "/api/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void 회원_가입에_성공한다() throws Exception {
        // given
        final UserSaveRequestDto request = new UserSaveRequestDto("username", "password", "nickname", "홍익대학교",
                "컴퓨터공학과");

        doNothing().when(userService).saveUser(request);

        // when & then
        mockMvc.perform(post(USER_DEFAULT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void 닉네임이_중복되면_실패한다() throws Exception {
        // given
        final UserSaveRequestDto request = new UserSaveRequestDto("username", "password", "nickname", "홍익대학교",
                "컴퓨터공학과");

        doThrow(new BadRequestException(ExceptionCode.ALREADY_EXIST_USERNAME)).when(userService).saveUser(request);

        // when & then
        mockMvc.perform(post(USER_DEFAULT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회원_탈퇴에_성공한다() throws Exception {
        // given
        doNothing().when(userService).deleteUser(anyLong());

        // when & then
        mockMvc.perform(delete(USER_DEFAULT_URL + "/{userId}", 1L))
                .andExpect(status().isOk());
    }
}
