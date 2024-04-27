package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.*;
import com.ceos19.everytime.dto.JoinUserRequest;
import com.ceos19.everytime.dto.CustomUserDetails;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ceos19.everytime.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final TimeTableRepository timeTableRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final TimeTableCourseRepository timeTableCourseRepository;
    private final PostRepository postRepository;
    private final ChatRepository chatRepository;
    private final CommentRepository commentRepository;
    private final SchoolRepository schoolRepository;
    private final PostLikeRepository postLikeRepository;
    private final BCryptPasswordEncoder encoder;

    public Long addUser(User user) {
        userRepository.findByUsername(user.getUsername())
                .ifPresent(f -> {
                    log.error("에러 내용: 유저 가입 실패 " +
                            "발생 원인: 이미 존재하는 아이디로 가입 시도");
                    throw new AppException(DATA_ALREADY_EXISTED, "이미 존재하는 아이디입니다");
                });
        userRepository.findByEmail(user.getEmail())
                .ifPresent(f -> {
                    log.error("에러 내용: 유저 가입 실패 " +
                            "발생 원인: 이미 존재하는 이메일로 가입 시도");
                    throw new AppException(DATA_ALREADY_EXISTED, "이미 사용중인 이메일입니다");
                });
        userRepository.findBySchoolIdAndStudentNo(user.getSchool().getId(), user.getStudentNo())
                .ifPresent(f -> {
                    log.error("에러 내용: 유저 가입 실패 " +
                            "발생 원인: 이미 존재하는 학번으로 가입 시도");
                    throw new AppException(DATA_ALREADY_EXISTED, "이미 사용중인 학번입니다");
                });

        userRepository.save(user);
        return user.getId();
    }

    public User join(JoinUserRequest request) {
        // 중복 검사
        userRepository.findByUsername(request.getUsername())
                .ifPresent(f -> {
                    log.error("에러 내용: 유저 가입 실패 " +
                            "발생 원인: 이미 존재하는 아이디로 가입 시도");
                    throw new AppException(DATA_ALREADY_EXISTED, "이미 존재하는 아이디입니다");
                });
        userRepository.findByEmail(request.getEmail())
                .ifPresent(f -> {
                    log.error("에러 내용: 유저 가입 실패 " +
                            "발생 원인: 이미 존재하는 이메일로 가입 시도");
                    throw new AppException(DATA_ALREADY_EXISTED, "이미 사용중인 이메일입니다");
                });
        userRepository.findBySchoolIdAndStudentNo(request.getSchoolId(), request.getStudentNo())
                .ifPresent(f -> {
                    log.error("에러 내용: 유저 가입 실패 " +
                            "발생 원인: 이미 존재하는 학번으로 가입 시도");
                    throw new AppException(DATA_ALREADY_EXISTED, "이미 사용중인 학번입니다");
                });


        School school = schoolRepository.findById(request.getSchoolId()).orElseThrow(() -> {
            log.error("에러 내용: 유저 가입 실패 " +
                    "발생 원인: 존재하지 않는 School PK로 조회");
            return new AppException(DATA_ALREADY_EXISTED, "존재하지 않은 학교입니다");
        });

        User user = User.builder()
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword()))
                .name(request.getName())
                .studentNo(request.getStudentNo())
                .email(request.getEmail())
                .role(request.getRole())
                .school(school)
                .build();
        userRepository.save(user);

        return user;
    }

    @Transactional(readOnly = true)
    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("에러 내용: 유저 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        });
    }

    @Transactional(readOnly = true)
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("에러 내용: 유저 조회 실패 " +
                    "발생 원인: 존재하지 않는 아이디 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("에러 내용: 유저 조회 실패 " +
                    "발생 원인: 존재하지 않는 아이디 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        });

        return new CustomUserDetails(user);
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("에러 내용: 유저 조회 실패 " +
                    "발생 원인: 존재하지 않는 이메일 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        });
    }

    @Transactional(readOnly = true)
    public User findUserBySchoolIdAndStudentNo(Long schoolId, String studentNo) {
        return userRepository.findBySchoolIdAndStudentNo(schoolId, studentNo).orElseThrow(() -> {
            log.error("에러 내용: 유저 조회 실패 " +
                    "발생 원인: 존재하지 않는 학교, 학번으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        });
    }

    @Transactional(readOnly = true)
    public List<User> findUserByName(String name) {
        return userRepository.findByName(name);
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
