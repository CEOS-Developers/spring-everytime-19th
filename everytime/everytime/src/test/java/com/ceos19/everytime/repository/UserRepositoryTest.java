package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.AboutUser.School;
import com.ceos19.everytime.domain.AboutUser.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;

    private School testSchool;

    @BeforeEach
    void setup() { //School 객체 미리 생성
        testSchool = new School("Sogang", 5000L);
        testSchool = entityManager.persistFlushFind(testSchool);
    }

    @Test
    @DisplayName("여러 user 저장 후 조회")
    public void testSaveAndFindMultipleUsers() {
        // Given
        User testUser1 = createUser("Chloe", "Business");
        User testUser2 = createUser("Soobin", "Computer Science Engineering");
        User testUser3 = createUser("Jennifer", "Philosophy");

        userRepository.save(testUser1);
        userRepository.save(testUser2);
        userRepository.save(testUser3);

        // When
        Optional<User> testedUser1 = userRepository.findByUsername(testUser1.getUsername());
        Optional<User> testedUser2 = userRepository.findByUsername(testUser2.getUsername());
        Optional<User> testedUser3 = userRepository.findByUsername(testUser3.getUsername());

        // Then
        assertTrue(testedUser1.isPresent(), "There is no user object.");// 사용자가 존재하는지 확인
        assertThat(testedUser1.get().getUsername()).isEqualTo(testUser1.getUsername());


        assertTrue(testedUser2.isPresent(), "There is no user object.");// 사용자가 존재하는지 확인
        assertThat(testedUser2.get().getUsername()).isEqualTo(testUser2.getUsername());

        assertTrue(testedUser3.isPresent(), "There is no user object.");// 사용자가 존재하는지 확인
        assertThat(testedUser3.get().getUsername()).isEqualTo(testUser3.getUsername());
    }

    private User createUser(String username, String department) {
        return User.builder()
                .username(username)
                .loginId(username + "1234")
                .loginPassword(username + "password")
                .nickname(username + "nick")
                .department(department)
                .studentId(20190000L)
                .email(username + "@gmail.com")
                .isActive(true)
                .school(testSchool)
                .loginAt(Timestamp.from(Instant.now()))
                .build();
    }
}
