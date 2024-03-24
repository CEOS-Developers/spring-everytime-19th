package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.AboutUser.School;
import com.ceos19.everytime.domain.AboutUser.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class Nplus1Test {

    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager em;

    @Test
    public void n_plus_one_test_1() throws Exception {
        School schoolA = new School("Sogang", 5000L);
        School schoolB = new School("Psick", 3000L);
        schoolRepository.save(schoolA);
        schoolRepository.save(schoolB);

        User user1 = createUser("user1", "department1", schoolA);
        User user2 = createUser("user2", "department2", schoolA);
        User user3 = createUser("user3", "department3", schoolB);
        User user4 = createUser("user4", "department4", schoolB);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

        em.flush(); // DB 에 쿼리 날림
        em.clear(); // 영속성 컨텍스트 캐시 초기화 (DB에서 가져오기 위함)

        // when
        List<User> users = userRepository.findAll();

        // then
        for (User user : users) {
            System.out.println("member = " + user.getUsername());
            System.out.println("member.getSchool().getSchoolName() = " + user.getSchgit reool().getSchoolName());
        }

    }

    private User createUser(String username, String department, School school) {
        return User.builder()
                .username(username)
                .loginId(username + "1234")
                .loginPassword(username + "password")
                .nickname(username + "nick")
                .department(department)
                .studentId(20190000L)
                .email(username + "@gmail.com")
                .isActive(true)
                .school(school)
                .loginAt(Timestamp.from(Instant.now()))
                .build();
    }
}
