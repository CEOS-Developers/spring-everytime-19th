package com.ceos19.springeverytime.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.springeverytime.domain.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserRepositoryTest {
    @Autowired UserRepository userRepository;

    @Test
    void findOne() {
        //given
        User user = new User();

        //when
        userRepository.save(user);
        User result = userRepository.findOne(user.getId());
        //then
        assertEquals(user.getId(), result.getId());
    }

    @Test
    void findAll() {
        //given
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        User user4 = new User();
        User user5 = new User();

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