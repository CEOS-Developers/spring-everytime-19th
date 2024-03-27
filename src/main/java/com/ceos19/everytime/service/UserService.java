package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.*;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.repository.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.ceos19.everytime.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final TimeTableRepository timeTableRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final TimeTableCourseRepository timeTableCourseRepository;
    private final PostRepository postRepository;
    private final ChatRepository chatRepository;
    private final CommentRepository commentRepository;
    private final SchoolRepository schoolRepository;
    private final PostLikeRepository postLikeRepository;
    private final EntityManager em;

    public Long addUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            log.error("에러 내용: 유저 가입 실패 " +
                    "발생 원인: 이미 존재하는 아이디로 가입 시도");
            throw new AppException(DATA_ALREADY_EXISTED, "이미 존재하는 아이디입니다");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            log.error("에러 내용: 유저 가입 실패 " +
                    "발생 원인: 이미 존재하는 이메일로 가입 시도");
            throw new AppException(DATA_ALREADY_EXISTED, "이미 사용중인 이메일입니다");
        }
        if (userRepository.findBySchoolIdAndStudentNo(user.getSchool().getId(), user.getStudentNo()).isPresent()) {
            log.error("에러 내용: 유저 가입 실패 " +
                    "발생 원인: 이미 존재하는 학번으로 가입 시도");
            throw new AppException(DATA_ALREADY_EXISTED, "이미 사용중인 학번입니다");
        }

        userRepository.save(user);
        return user.getId();
    }

    @Transactional(readOnly = true)
    public User findUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            log.error("에러 내용: 유저 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        }
        return optionalUser.get();
    }

    @Transactional(readOnly = true)
    public User findUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            log.error("에러 내용: 유저 조회 실패 " +
                    "발생 원인: 존재하지 않는 아이디 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        }

        return optionalUser.get();
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            log.error("에러 내용: 유저 조회 실패 " +
                    "발생 원인: 존재하지 않는 이메일 값으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        }
        return optionalUser.get();
    }

    @Transactional(readOnly = true)
    public User findUserBySchoolIdAndStudentNo(Long schoolId, String studentNo) {
        Optional<User> optionalUser = userRepository.findBySchoolIdAndStudentNo(schoolId, studentNo);
        if (optionalUser.isEmpty()) {
            log.error("에러 내용: 유저 조회 실패 " +
                    "발생 원인: 존재하지 않는 학교, 학번으로 조회");
            throw new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        }
        return optionalUser.get();
    }

    public void removeUser(Long userId) {
        List<TimeTable> timeTables = timeTableRepository.findByUserId(userId);
        for (TimeTable timeTable : timeTables) {
            timeTableCourseRepository.deleteAllByTimeTableId(timeTable.getId());
        }
        timeTableRepository.deleteAll(timeTables);

        List<Post> posts = postRepository.findByAuthorId(userId);

        for (Post post : posts) {
            // Post와 연관된 PostLike 제거
            List<PostLike> postLikes = postLikeRepository.findByPostId(post.getId());
            postLikeRepository.deleteAllByPostId(post.getId());

            // Post와 연관된 Comment 제거
            List<Comment> comments = commentRepository.findByPostId(post.getId());
            for (Comment comment : comments) {
                comment.removeParentComment();
            }
            commentRepository.deleteAll(comments);
        }
        postRepository.deleteAllByAuthorId(userId);

        // User가 다른 Post에 쓴 comments 제거
        List<Comment> comments = commentRepository.findByCommenterId(userId);
        for (Comment comment : comments) {
            comment.removeParentComment();
        }
        commentRepository.deleteAllByCommenterId(userId);

        // 유저가 속한 채팅방 제거
        List<ChattingRoom> chattingRooms = chattingRoomRepository.findByParticipantId(userId);
        for (ChattingRoom chattingRoom : chattingRooms) {
            chatRepository.deleteAllByChattingRoomId(chattingRoom.getId());
            chattingRoomRepository.deleteById(userId);
        }

        userRepository.deleteById(userId);
    }
}
