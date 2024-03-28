package com.ceos19.springeverytime.user.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.springeverytime.user.domain.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void findOne() {
        //given
        User user = User.builder()
                .build();
        //when
        userRepository.save(user);
        User result = userRepository.findById(user.getId()).orElseThrow(IllegalStateException::new);

        //then
        assertEquals(user.getId(), result.getId());
    }

    @Test
    void findAll() {
        //given
        User user1 = User.builder()
                .build();
        User user2 = User.builder()
                .build();
        User user3 = User.builder()
                .build();
        User user4 = User.builder()
                .build();
        User user5 = User.builder()
                .build();

        //when
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);

        List<User> allUsers = userRepository.findAll();
        //then

        assertEquals(5, allUsers.size());
    }
}