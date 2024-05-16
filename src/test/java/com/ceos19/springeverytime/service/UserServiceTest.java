package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.common.EntityGenerator;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.user.dto.request.UserCreateRequest;
import com.ceos19.springeverytime.domain.user.dto.response.CustomUserDetails;
import com.ceos19.springeverytime.domain.user.repository.UserRepository;
import com.ceos19.springeverytime.domain.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void setup() {
        CustomUserDetails userDetails = new CustomUserDetails(EntityGenerator.generateUser("test"));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }


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

    @Test
    @DisplayName("유저 회원 탈퇴 테스트")
    void 회원탈퇴_테스트() {
        // given
        User user = EntityGenerator.generateUser("test");
        when(userRepository.findByLoginId(anyString())).thenReturn(Optional.of(user));

        // when
        userService.delete();

        // then
        verify(userRepository, times(1)).delete(user);
    }
}
