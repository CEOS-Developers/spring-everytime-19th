package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.*;
import jakarta.persistence.EntityManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class PostRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    EntityManager em;

    User user;
    User user2;
    Post post;

    @Before
    public void each() {
        user = new User("user1", "a123456", "asdf@asdf.com", "asdf");
        user2 = new User("user2", "a654321", "asdfasfd@asdf.com", "asdf");
        post = new Post("title", "content");

        userRepository.save(user);
        userRepository.save(user2);
        postRepository.save(post);
    }

    @Test
    public void heartTest() throws Exception {
        //given
        System.out.println(post);

        //when
        post.addHeartFromUser(user);
        em.flush();

        //then
        User user1 = userRepository.findById(user.getId()).get();
        assertEquals(post.getHearts().get(0).getId(), user1.getHearts().get(0).getId());
        System.out.println("user1.getHearts().get(0).getId() = " + user1.getHearts().get(0).getId());
    }

    @Test
    public void heartDeleteTest() throws Exception {
        //given
        //when
        post.addHeartFromUser(user);
        em.flush();

        Heart heart = post.getHearts().get(0);
        post.removeHeartFromUserAndPost(heart);
        em.flush();

        //then
        assertEquals(0, post.getHearts().size());
        assertEquals(0, user.getHearts().size());
    }

    @Test
    public void commentTest() throws Exception {
        //given
        post.addCommentFromUser(user, "hello");
        em.flush();

        //when
        Comment commentFromUser = user.getComments().get(0);
        Comment commentFromPost = post.getComments().get(0);

        //then
        assertEquals(commentFromUser.getId(), commentFromPost.getId());  //Post에 Comment 등록시 User에도 등록
        System.out.println(commentFromUser.getId()+", "+commentFromPost.getId());
    }

    @Test
    public void commentDeleteTest() throws Exception {
        //given
        post.addCommentFromUser(user, "hello");
        em.flush();

        post.removeCommentFromUserAndPost(post.getComments().get(0));

        assertEquals(0, post.getComments().size());
        assertEquals(0, user.getComments().size());
    }

    @Test
    public void addReplyTest() throws Exception{
        //given
        post.addCommentFromUser(user, "content");
        Comment comment = post.getComments().get(0);
        //when
        comment.addReplyFromUser(user2,"so good~~");

        //then
        assertEquals("so good~~",comment.getReplies().get(0).getContent());
        assertEquals("so good~~",user2.getReplies().get(0).getContent());
        assertEquals(user2.getId(), comment.getReplies().get(0).getUser().getId());
    }
    @Test
    public void replyDeleteTest() throws Exception{
        //given
        post.addCommentFromUser(user, "content");
        Comment comment = post.getComments().get(0);
        //when
        comment.addReplyFromUser(user2,"so good~~");

        //then
        comment.removeReplyFromUserAndComment(comment.getReplies().get(0));
        assertEquals(0, comment.getReplies().size());
        assertEquals(0, user2.getReplies().size());

    }
}
