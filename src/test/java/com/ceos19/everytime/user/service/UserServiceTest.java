package com.ceos19.everytime.user.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ceos19.everytime.user.domain.School;
import com.ceos19.everytime.user.dto.request.UserSaveRequestDto;
import com.ceos19.everytime.user.repository.SchoolRepository;
import com.ceos19.everytime.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void 회원가입에_성공한다() {
        // given
        final UserSaveRequestDto request = new UserSaveRequestDto("username", "1234", "nick", "홍익대학교", "컴퓨터공학과");

        given(schoolRepository.findByNameAndDepartment(anyString(), anyString()))
                .willReturn(Optional.of(new School("홍익대학교", "컴퓨터공학과")));

        // when
        userService.saveUser(request);

        // then
        verify(userRepository).save(any());
    }
}
