package com.ceos19.everytime.repository;

import com.ceos19.everytime.domain.*;
import com.ceos19.everytime.service.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class CommentRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    SchoolRepository schoolRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    TimeTableRepository timeTableRepository;
    @Autowired
    TimeTableCourseRepository timeTableCourseRepository;
    @Autowired
    ChattingRoomRepository chattingRoomRepository;
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    EntityManager em;

    User user1;
    Post post;

    @BeforeEach
    public void each() {
        // 학교 저장
        School school = new School("홍익대학교");
        schoolRepository.save(school);

        // 게시판 저장
        Board board = new Board("컴공게시판", school);
        boardRepository.save(board);

        user1 = new User("myUsername", "myPassword", "김상덕", "A000011", "um@naver.com", school);
        userRepository.save(user1);

        post = new Post("새로운 포스팅", "ㅈㄱㄴ", false, false, board, user1);


        Attachment attachment = Attachment.builder()
                .originFileName("original")
                .storePath("/usr")
                .attachmentType(AttachmentType.IMAGE)
                .build();

        post.addAttachment(attachment);
        post.addAttachment(attachment);

        Comment comment1 = new Comment("ㅎㅇ요1", user1, post, null);
        Comment comment2 = new Comment("ㅎㅇ요2", user1, post, null);
        Comment comment3 = new Comment("ㅎㅇ요3", user1, post, comment2);

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        postRepository.save(post);
    }

    @Test
    public void comment제거() throws Exception {
        //given
        List<Comment> comments = commentRepository.findByPostId(post.getId());
        for (Comment comment : comments) {
            comment.removeParentComment();
        }
        commentRepository.deleteAll(comments);
    }
}