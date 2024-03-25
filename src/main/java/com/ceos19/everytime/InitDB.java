package com.ceos19.everytime;

import com.ceos19.everytime.domain.*;
import com.ceos19.everytime.repository.*;
import com.ceos19.everytime.service.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.ceos19.everytime.domain.Weekend.FRI;
import static com.ceos19.everytime.domain.Weekend.TUE;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct  // 빈으로 등록될 시 자동으로 실행됨
    public void init() {
        initService.init();
        initService.deleteUser();
//        initService.deletePost();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final SchoolService schoolService;
        private final BoardRepository boardRepository;
        private final UserService userService;
        private final CourseRepository courseRepository;
        private final TimeTableService timeTableService;
        private final TimeTableCourseRepository timeTableCourseRepository;
        private final ChattingRoomService chattingRoomService;
        private final ChatService chatService;
        private final ChatRepository chatRepository;
        private final PostService postService;
        private final PostLikeService postLikeService;
        private final CommentService commentService;
        private final EntityManager em;

        public void init() {
            // 학교 저장
            School school = new School("홍익대학교");
            schoolService.save(school);

            // 게시판 저장
            Board board = new Board("컴공게시판", school);
            boardRepository.save(board);

            // 과목 저장
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
            User user1 = new User("myUsername", "myPassword", "김상덕", "A000011", "um@naver.com", school);
            userService.join(user1);
            User user2 = new User("yourUsername", "myPassword", "김상덕", "A000012", "um1@naver.com", school);
            userService.join(user2);

            // 시간표 생성
            TimeTable timeTable1 = new TimeTable("22년 2학기", 2022, Semester.SECOND, user1);
            TimeTable timeTable2 = new TimeTable("23년 1학기", 2023, Semester.FIRST, user1);
            timeTableService.save(timeTable1);
            timeTableService.save(timeTable2);

            // 시간표에 수업 추가
            TimeTableCourse timeTableCourse1 = new TimeTableCourse(timeTable1, course1);
            TimeTableCourse timeTableCourse2 = new TimeTableCourse(timeTable1, course2);
            TimeTableCourse timeTableCourse3 = new TimeTableCourse(timeTable2, course1);
            timeTableCourseRepository.save(timeTableCourse1);
            timeTableCourseRepository.save(timeTableCourse2);
            timeTableCourseRepository.save(timeTableCourse3);

            // ChattingRoom 생성
            ChattingRoom chattingRoom = new ChattingRoom(user1, user2);
            chattingRoomService.save(chattingRoom);

            // Chat 생성
            Chat chat1 = new Chat("안녕?", user1, chattingRoom);
            Chat chat2 = new Chat("반가워", user2, chattingRoom);
            Chat chat3 = new Chat("안녕 ㅎㅎ", user1, chattingRoom);
            chatService.save(chat1);
            chatService.save(chat2);
            chatService.save(chat3);

            // Post 생성
            Post post = new Post("새로운 포스팅", "ㅈㄱㄴ", false, false, board, user1);
            // Post에 사진 추가
            Attachment attachment1 = Attachment.builder()
                    .originFileName("사진")
                    .storePath("/usr")
                    .attachmentType(AttachmentType.IMAGE)
                    .build();
            post.addAttachment(attachment1);
            Attachment attachment2 = Attachment.builder()
                    .originFileName("사진")
                    .storePath("/usr")
                    .attachmentType(AttachmentType.IMAGE)
                    .build();
            post.addAttachment(attachment2);
            postService.save(post);

            // Post에 좋아요 추가
            postLikeService.save(post.getId(), user1.getId());
            postLikeService.save(post.getId(), user2.getId());

            Comment comment1 = new Comment("comment1", user2, post, null);
            Comment comment2 = new Comment("comment2", user2, post, null);
            commentService.save(comment1);
            commentService.save(comment2);
            Comment reply = new Comment("reply", user2, post, comment2);
            commentService.save(reply);
        }

        public void deleteUser() {
//            userService.deleteUser(1L);
        }

        public void deletePost() {
            postService.deletePost(1L);
        }
    }
}


