package com.ceos19.springboot.domain;

import com.ceos19.springboot.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UsersTest {
    @Autowired
    UserRepository userRepository;
    @Test
    public void createUser() throws Exception {
        //given
        Users user = Users.builder()
                .university("홍익대")
                .email("email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("정기민")
                .password("비번")
                .build();
        //when
        Users savedUser = userRepository.save(user);
        //then
        Users findUser = userRepository.findByUsername("정기민");
        Assertions.assertThat(savedUser.getUsername()).isEqualTo(findUser.getUsername());
    }
}