package com.ceos19.springboot.repository;

import com.ceos19.springboot.domain.Friend;
import com.ceos19.springboot.domain.School;
import com.ceos19.springboot.domain.User;
import com.ceos19.springboot.repository.FriendRepository;
import com.ceos19.springboot.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendRepository friendRepository;

    User insertUser1;
    User insertUser2;
    User insertUser3;

    Friend friend1;
    Friend friend2;
    Friend friend3;

    @BeforeEach
    void setup() {
        System.err.println("=============================== setup ==============================");
        School univ = new School("Yonsei");

        insertUser1 = userRepository.save(User.builder()
                .username("yeonghwan")
                .password("testpw")
                .nickname("yeonghwan")
                .isAdmin(true)
                .userLast("Jang")
                .userFirst("Yeonghwan")
                .email("jghff700@naver.com")
                .isBoardManager(false)
                .board("computerGallery")
                .isBanned(false)
                .school(univ)
                .build()
        );

        insertUser2 = userRepository.save(User.builder()
                .username("jihoon")
                .password("testpw")
                .nickname("jihoon")
                .isAdmin(true)
                .userLast("Kim")
                .userFirst("jihoon")
                .email("imjihoon@naver.com")
                .isBoardManager(false)
                .board("computerGallery")
                .isBanned(false)
                .school(univ)
                .build()
        );

        insertUser3 = userRepository.save(User.builder()
                .username("jin")
                .password("testpw")
                .nickname("jinn")
                .isAdmin(true)
                .userLast("Jun")
                .userFirst("jin")
                .email("hellojj@naver.com")
                .isBoardManager(false)
                .board("computerGallery")
                .isBanned(false)
                .school(univ)
                .build()
        );

        friend2 = friendRepository.save(Friend.builder()
                .isAccepted(true)
                .myId(insertUser1.getUserId())
                .friendUser(insertUser2)
                .build()
        );

        friend3 = friendRepository.save(Friend.builder()
                .isAccepted(true)
                .myId(insertUser1.getUserId())
                .friendUser(insertUser3)
                .build()
        );

    }

    @Test
    @DisplayName("정상 회원 가입 케이스 테스트")
    @Transactional
    void joinUser() {
        // given
        User insertUser = insertUser1;

        // when
        User saveUser = userRepository.findByUsername("yeonghwan").get();
//        System.out.println(saveUser);

        // then
        assertThat(insertUser).isEqualTo(saveUser);
    }

    @Test
    @Transactional
    @DisplayName("N+1 문제 테스트")
    void n1IssueTracking() {
        List<Friend> friendList = friendRepository.findAll();
        System.out.println("total friend data size:" + friendList.size());
    }
}
