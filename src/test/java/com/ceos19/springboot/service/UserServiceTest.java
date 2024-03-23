package com.ceos19.springboot.service;

import com.ceos19.springboot.domain.Comment;
import com.ceos19.springboot.domain.Post;
import com.ceos19.springboot.domain.School;
import com.ceos19.springboot.domain.Users;
import com.ceos19.springboot.repository.CommentRepository;
import com.ceos19.springboot.repository.PostRepository;
import com.ceos19.springboot.repository.SchoolRepository;
import com.ceos19.springboot.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;

    private School saveSchool;
    private Users saveUser;
    private Post savePost;
    @BeforeEach
    public void setUp() {
        School school = School.builder()
                .schoolName("홍익대학교")
                .dept("컴퓨터공학과")
                .build();
        saveSchool = schoolRepository.save(school);

        Users user = Users.builder()
                .university(saveSchool)
                .email("email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("정기민")
                .password("비번")
                .build();
        saveUser = userRepository.save(user);

        Post post1 = Post.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글1 내용")
                .imageUrl("image url")
                .title("게시글1 제목")
                .likes(0)
                .user(saveUser)
                .build();
        savePost = postRepository.save(post1);
    }

    @Test
    @DisplayName("사용자 저장? 테스트")
    public void userSaveTest() throws Exception {
        //given
        Users user = Users.builder()
                .university(saveSchool)
                .email("email")
                .loginId("아이디")
                .nickname("test account")
                .username("tester")
                .password("비번")
                .build();
        //when
        Long savedUserId = userService.saveUser(user);
        Users findUser = userRepository.findById(savedUserId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID를 가진 사용자가 없습니다"));
        //then
        Assertions.assertThat(findUser.getUsername()).isEqualTo("tester");
    }

    @Test
    @DisplayName("사용자 탈퇴 테스트")
    public void userDeleteTest() throws Exception {
        //given
        Comment comment = Comment.builder()
                .content("댓글 내용ㅇ오오옹ㅇ")
                .user(saveUser)
                .post(savePost)
                .build();
        commentRepository.save(comment);
        //when
        userService.deleteUser(saveUser);
        //then
        
    }
}