package com.ceos19.everytime.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.everytime.domain.*;
import com.ceos19.everytime.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class PostRepositoryTest {
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

    User user1;

    @BeforeEach
    public void each() {
        // 학교 저장
        School school = new School("홍익대학교");
        schoolRepository.save(school);

        // 게시판 저장
        Board board = new Board("컴공게시판", school);
        boardRepository.save(board);

        user1 = new User("myUsername", "myPassword", "김상덕", "A000011", "um@naver.com", school,"ROLE_ADMIN");
        userRepository.save(user1);

        Post post = new Post("새로운 포스팅", "ㅈㄱㄴ", false, false, board, user1);


        Attachment attachment = Attachment.builder()
                .originFileName("original")
                .storePath("/usr")
                .attachmentType(AttachmentType.IMAGE)
                .build();

        post.addAttachment(attachment);
        postRepository.save(post);


        Comment comment1 = new Comment("ㅎㅇ요1", user1, post, null);
        Comment comment2 = new Comment("ㅎㅇ요2", user1, post, null);
        Comment reply = new Comment("ㅎㅇ요3", user1, post, comment2);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
    }

    @Test
    public void 포스트제거() throws Exception {
        //given
        List<Post> byAuthorId = postRepository.findByAuthorId(user1.getId());
        for (Post post : byAuthorId) {
            List<Comment> comments = commentRepository.findByPostId(post.getId());
            for (Comment comment : comments) {
                comment.removeParentComment();
            }
            commentRepository.deleteAll(comments);
        }

        postRepository.deleteAll(byAuthorId);


        List<Post> all = postRepository.findByAuthorId(user1.getId());
        for (Post post : all) {
            System.out.println("all post.getId() = " + post.getId());
        }
        System.out.println(all.size());
    }

}