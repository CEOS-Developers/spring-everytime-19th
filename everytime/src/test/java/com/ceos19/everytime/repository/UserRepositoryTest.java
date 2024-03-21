package com.ceos19.everytime.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.everytime.domain.*;
import jakarta.persistence.EntityManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SchoolRepository schoolRepository;
    @Autowired
    PostRepository postRepository;

    @Autowired
    EntityManager em;

    School school;
    School school2;

    User testUser;
    Post testPost;

    @Before
    public void init() {
        school = new School("서울대학교", "컴공");
        school2 = new School("홍익대학교", "미술");
        schoolRepository.save(school);
        schoolRepository.save(school2);

        testUser = new User("엄준식", "B111111", "umjoonsik@naver.com", "pass");
        userRepository.save(testUser);

        testPost = new Post("title", "content");
        postRepository.save(testPost);
    }

    @Test
    public void saveUserTest() throws Exception{
        //given
        User user1 = new User("엄준식", "B111111", "umjoonsik@naver.com", "pass");
        User user2 = new User("엄준식", "A000000", "umjoonsik@naver.com", "pass");

        //when

        //then
        Assert.assertThrows(Exception.class, () -> userRepository.save(user1));  // 동일한 학번 저장시 오류
        Assert.assertThrows(Exception.class, () -> userRepository.save(user2));  // 동일한 username 저장시 오류
    }
    @Test
    public void findUserTest() throws Exception {
        //given
        User chulsu = null;
        User yunghee = null;
        User minsu = null;

        User user1 = new User("철수", "B111111", "chulsu@naver.com", "password1", school);
        User user2 = new User("영희", "B111112", "yunghee@naver.com", "password2", school);
        User user3 = new User("민수", "B111113", "minsu@naver.com", "password3", school2);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        //when
        Optional<User> optionalChulsu = userRepository.findByUsername("chulsu@naver.com");
        Optional<User> optionalYunghee = userRepository.findByUsername("yunghee@naver.com");
        Optional<User> optionalMinsu = userRepository.findByUsername("minsu@naver.com");


        if (optionalChulsu.isPresent()) chulsu = optionalChulsu.get();
        else fail();
        if (optionalMinsu.isPresent()) minsu = optionalMinsu.get();
        else fail();
        if (optionalYunghee.isPresent()) yunghee = optionalYunghee.get();
        else fail();

        //then
        assertEquals(user1.getId(), chulsu.getId());
        assertEquals(user2.getId(), yunghee.getId());
        assertEquals(user3.getId(), minsu.getId());

        assertEquals(user1.getSchool().getName(), "서울대학교");
    }

    @Test
    public void schoolTest() throws Exception {
        //given
        User user = new User("user", "123", "mmm@gmail.com", "asdf", school);

        //when
        userRepository.save(user);
        List<User> all = userRepository.findAll();

        //then

        assertEquals(user.getSchool().getName(), "school");
    }

    @Test
    public void test2() throws Exception {
        //given
        User user = new User("user", "123", "mmm@gmail.com", "asdf", school);

        //when
        userRepository.save(user);

        //then
        assertEquals(userRepository.findByName("user").get().getName(), "user");
    }

    @Test
    public void test3() throws Exception {
        //given
        User user1 = new User("user1", "123", "mmm@gmail.com", "asdf", school);
        User user2 = new User("user2", "5321", "mmm123@gmail.com", "asdf", school);
        userRepository.save(user1);
        userRepository.save(user2);

        //when
        Optional<User> byId1 = userRepository.findById(user1.getId());
        Optional<User> byId2 = userRepository.findById(user2.getId());

        User sender = byId1.get();
        User receiver = byId2.get();
        Message message = new Message("asdf", "124werfad");
        message.setSenderAndReceiver(sender, receiver);

        //then
        System.out.println("id=" + message.getId());
    }

    @Test
    public void messageTest() throws Exception {
        //given
        User sender = new User("user1", "1213423452", "mmm@gmail.com", "asdf", school);
        User receiver = new User("user2", "124321", "mmm123@gmail.com", "asdf", school);
        userRepository.save(sender);
        userRepository.save(receiver);

        //when
        Message message = new Message("asdf", "vdcxzweat");
        message.setSenderAndReceiver(sender, receiver);

        //then
        List<Message> sendMessages = sender.getSendMessages();
        for (Message sendMessage : sendMessages) {
            System.out.println("sendMessage = " + sendMessage);
        }


        Assert.assertEquals(sender.getSendMessages().get(0).getReceiver().getId(), receiver.getId());
    }

    //delete test
    @Test
    public void deleteUser() throws Exception {
        //given

        //when
        userRepository.delete(testUser);
        //then
        Optional<User> byId = userRepository.findById(testUser.getId());
        if (byId.isPresent()) {
            fail("삭제 안됨");
        }
    }

}