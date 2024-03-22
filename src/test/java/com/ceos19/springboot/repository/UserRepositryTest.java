package com.ceos19.springboot.repository;

import com.ceos19.springboot.domain.School;
import com.ceos19.springboot.domain.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        System.err.println("=============================== setup ==============================");
    }

    @Test
    @DisplayName("정상 회원 가입 케이스 테스트")
    @Transactional
    void joinUser() {
        // given
        LocalDateTime currentDateTime = LocalDateTime.now();

        School univ = new School("Yonsei");

        User insertUser = userRepository.save(User.builder()
                .username("yeonghwan")
                .password("testpw")
                .nickname("yeonghwan")
                .isAdmin(true)
                .userLast("Jang")
                .userFirst("Yeonghwan")
                .email("jghff700@naver.com")
                .isBoardManager(false)
                .board(null)
                .isBanned(false)
                .createdAt(currentDateTime)
                .lastLogin(currentDateTime)
                .school(univ).build()
        );

        // when
        User saveUser = userRepository.findByUsername("yeonghwan").get();

        // then
        assertThat(insertUser).isEqualTo(saveUser);
    }
}
