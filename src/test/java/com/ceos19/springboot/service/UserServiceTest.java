package com.ceos19.springboot.service;

import com.ceos19.springboot.domain.School;
import com.ceos19.springboot.domain.User;
import com.ceos19.springboot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
//@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

//    @InjectMocks
    @Autowired
    UserService userService;

//    @Mock
    @Autowired
    UserRepository userRepository;

    User signUpUser;

    @BeforeEach
    void setup() {

//        System.err.println("=============================== setup ==============================");

        School univ = new School("Yonsei");

        signUpUser = userRepository.save(User.builder()
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
    }


    @Test
    @Transactional
    @DisplayName("Sign in Test")
    void singInTest() throws Exception {
        // given
        String username = signUpUser.getUsername();

        // when
        userService.addUser(signUpUser);
        User findUser = userRepository.findByUsername(username).get();

        // then
        assertEquals(signUpUser.getUsername(), findUser.getUsername());
        assertEquals(signUpUser.getNickname(), findUser.getNickname());
        assertEquals(signUpUser.getPassword(), findUser.getPassword());
    }

}
