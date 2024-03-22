package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class UserRepositoryTest {
    @Test
    @DisplayName("유저 생성 테스트")
    void 유저_생성_테스트() {
        // given
        User user = generateUser("id1");

        // when
        User saveUser = userRepository.save(user);

        // then
        assertEquals(saveUser.getLoginId(), "id1");
        assertEquals(saveUser.getPw(), "1234");
        assertEquals(saveUser.getName(), "name");
    }

    @Test
    @DisplayName("유저 삭제 테스트")
    void 유저_삭제_테스트() {
        // given
        User user = generateUser("id1");
        User saveUser = userRepository.save(user);

        // when
        userRepository.delete(saveUser);

        // then
        Optional<User> findUser = userRepository.findByLoginId("id1");
        assertTrue(findUser.isEmpty());
    }

    @Autowired
    UserRepository userRepository;

    private User generateUser(String userLoginId) {
        return new User(
            userLoginId,
            "1234",
            "test",
            "name",
            "computer",
            "20",
            "test@example.com"
        );
    }

}