package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Category;
import com.ceos19.springeverytime.domain.Comment;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@SpringBootTest
@Transactional
public class CommentRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    public void 댓글_생성_조회_테스트() throws Exception {
        //given
        Post post1 = postRepository.findOne(5L);
        User user1 = userRepository.findOne("user1");

        //when
        Comment comment1 = new Comment("댓글1",true, user1, post1);
        Comment comment2 = new Comment("댓글2",true, user1, post1);
        Comment comment3 = new Comment("댓글3",true, user1, post1);

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        em.flush();
        em.clear();

        //then
        Assertions.assertEquals(3, em.find(Post.class, post1.getPostId()).getComments().size());
    }
}
