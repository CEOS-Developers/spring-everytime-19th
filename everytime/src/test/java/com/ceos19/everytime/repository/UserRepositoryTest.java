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
public class UserRepositoryTest{

    @Autowired
    UserRepository userRepository;
    @Autowired
    SchoolRepository schoolRepository;
    @Autowired
    PostRepository postRepository;

    @Autowired
    EntityManager em;

    School school;

    User testUser;
    Post testPost;

    @Before
    public void init() {
        school = new School("school", "department");
        schoolRepository.save(school);

        testUser = new User("엄준식", 11534111, "umjoonsik@naver.com", "pass");
        userRepository.save(testUser);

        testPost = new Post("title", "content");
        postRepository.save(testPost);
    }

    @Test
    public void test1() throws Exception{
        //given
        User user = new User("user",123,"mmm@gmail.com","asdf",school);

        //when
        userRepository.save(user);
        List<User> all = userRepository.findAll();

        //then

        assertEquals(user.getSchool().getName(),"school");
    }

    @Test
    public void test2() throws Exception{
        //given
        User user = new User("user",123,"mmm@gmail.com","asdf",school);

        //when
        userRepository.save(user);

        //then
        assertEquals(userRepository.findByName("user").get().getName(), "user");
    }

    @Test
    public void test3() throws Exception{
        //given
        User user1 = new User("user1",123,"mmm@gmail.com","asdf",school);
        User user2 = new User("user2",5321,"mmm123@gmail.com","asdf",school);
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
    public void messageTest() throws Exception{
        //given
        User sender = new User("user1",1213423452,"mmm@gmail.com","asdf",school);
        User receiver = new User("user2",124321,"mmm123@gmail.com","asdf",school);
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


        Assert.assertEquals(sender.getSendMessages().get(0).getReceiver().getId(),receiver.getId());
    }

    //delete test
    @Test
    public void deleteUser() throws Exception{
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