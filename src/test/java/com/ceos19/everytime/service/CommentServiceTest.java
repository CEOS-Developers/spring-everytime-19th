package com.ceos19.everytime.service;

import static com.ceos19.everytime.domain.Weekend.FRI;
import static com.ceos19.everytime.domain.Weekend.TUE;
import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.everytime.domain.*;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.repository.*;
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
class CommentServiceTest {
    @Autowired
    SchoolRepository schoolRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    UserService userService;
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
    CommentService commentService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostService postService;
    @Autowired
    CommentRepository commentRepository;

    Long commentId;

    @BeforeEach
    public void each() {
        // 학교 저장
        School school = new School("홍익대학교");
        schoolRepository.save(school);

        // 게시판 저장
        Board board = new Board("컴공게시판", school);
        boardRepository.save(board);

        // 수업 저장
        Course course1 = new Course("1234-123", "갈비탕개론", 2, "미스터갈비탕교수", 3, "t123", school);
        course1.addClassTime(FRI, 5);
        course1.addClassTime(FRI, 6);
        course1.addClassTime(FRI, 7);

        Course course2 = new Course("4321-123", "라면학개론", 4, "미스터라면교수", 3, "t203", school);
        course2.addClassTime(TUE, 3);
        course2.addClassTime(TUE, 4);
        course2.addClassTime(TUE, 6);
        courseRepository.save(course1);
        courseRepository.save(course2);

        // 유저 가입
        User user1 = new User("myUsername", "myPassword", "엄준식", "A000011", "um@naver.com", school,"ROLE_ADMIN");
        userService.addUser(user1);
        User user2 = new User("yourUsername", "myPassword", "김상덕", "A000012", "kim@naver.com", school,"ROLE_ADMIN");
        userService.addUser(user2);

        Post post = new Post("포스트1", "내용1", false, false, board, user1);
        postService.addPost(post);
        Long postId = post.getId();

        Comment comment1 = new Comment("코멘트1", user1, post, null);
        Comment comment2 = new Comment("코멘트2", user2, post, null);
        Comment reply = new Comment("대댓글", user1, post, comment2);
        Comment reply2 = new Comment("대댓글2", user2, post, reply);
        commentId = commentService.addComment(comment1);
        commentService.addComment(comment2);
        commentService.addComment(reply);
        commentService.addComment(reply2);
    }

    @Test
    public void deleteComment() throws Exception{
        //given
        commentService.removeComment(commentId);
        //when

        //then
        List<Comment> comments = commentRepository.findAll();
        assertEquals(comments.size(),3);
        assertThrows(AppException.class, () -> commentService.findCommentById(commentId));
    }
}