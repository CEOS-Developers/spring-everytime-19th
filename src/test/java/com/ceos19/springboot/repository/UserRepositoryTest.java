package com.ceos19.springboot.repository;

import com.ceos19.springboot.users.domain.Users;
import com.ceos19.springboot.users.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void userSaveTest() throws Exception {
        //given
        Users user = Users.builder()
                .username("rlals")
                .nickname("rlals")
                .build();
        //when
        userRepository.save(user);
        Users findUser = userRepository.findById(user.getUserId()).orElseThrow(() -> new EntityNotFoundException("없음"));
        //then
        Assertions.assertThat(user.getUsername()).isEqualTo(findUser.getUsername());
    }

}
