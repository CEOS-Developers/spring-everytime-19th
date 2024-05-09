package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.common.EntityGenerator;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.user.dto.request.UserCreateRequest;
import com.ceos19.springeverytime.domain.user.repository.UserRepository;
import com.ceos19.springeverytime.domain.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("회원 가입 테스트")
    void 회원가입_테스트() {
        // given
        User user = EntityGenerator.generateUser("test1");
        UserCreateRequest request = new UserCreateRequest(
            "test1",
            "1234",
            "nickname",
            "kim",
            "computer",
            "20",
            "test@example.com"
        );
        given(userRepository.save(any(User.class))).willReturn(user);
        given(userRepository.existsByLoginId(anyString())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn("encodedPW");

        // when
        User joinUser = userService.register(request);

        // then
        Assertions.assertThat(joinUser).isSameAs(user);
    }
}
