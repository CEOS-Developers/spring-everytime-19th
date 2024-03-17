package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.School;
import com.ceos19.everytime.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;


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
        testSchool = new School("Sogang", Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()), 5000L);
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
        User testedUser1 = userRepository.findByUsername(testUser1.getUsername());
        User testedUser2 = userRepository.findByUsername(testUser2.getUsername());
        User testedUser3 = userRepository.findByUsername(testUser3.getUsername());

        // Then
        assertNotNull(testedUser1);//해당 객체가 null이 아닌지 확인 -> null이 아니라면 정상 작동된 것
        assertNotEquals(testUser1.getUsername(), testedUser1.getUsername(), "It is not expected result."); //기대되는 객체와 테스트로 나온 실제 객체의 값이 같은지 확인

        assertNotNull(testedUser2);//해당 객체가 null이 아닌지 확인 -> null이 아니라면 정상 작동된 것
        assertNotEquals(testUser2.getUsername(), testedUser2.getUsername(), "It is not expected result."); //기대되는 객체와 테스트로 나온 실제 객체의 값이 같은지 확인

        assertNotNull(testedUser3);//해당 객체가 null이 아닌지 확인 -> null이 아니라면 정상 작동된 것
        assertNotEquals(testUser3.getUsername(), testedUser3.getUsername(), "It is not expected result."); //기대되는 객체와 테스트로 나온 실제 객체의 값이 같은지 확인
    }

    private User createUser(String username, String department) {
        return User.builder()
                .username(username)
                .login_id(username + "1234")
                .login_password(username + "password")
                .nickname(username + "nick")
                .department(department)
                .student_id(20200000L)
                .email(username + "@gmail.com")
                .is_active(true)
                .school(testSchool)
                .join_at(Timestamp.from(Instant.now()))
                .login_at(Timestamp.from(Instant.now()))
                .updated_at(Timestamp.from(Instant.now()))
                .latest_login_at(Timestamp.from(Instant.now()))
                .build();
    }
}
