package com.ceos19.springeverytime.service;

import com.ceos19.springeverytime.domain.User;
import com.ceos19.springeverytime.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    void test() {
        // given
        User user = new User(
            "test",
            "adsfbsa234@ad",
            "nicnname",
            "kim",
            "computer",
            "20",
            "test@exmaple.com",
            true
        );
        given(userRepository.findOne(any())).willReturn(user);

        // when
        System.out.println(user.getUserId());
        User test_user = userService.findOne(user.getUserId());

        // then
        Assertions.assertThat(test_user).isSameAs(user);
    }

}
