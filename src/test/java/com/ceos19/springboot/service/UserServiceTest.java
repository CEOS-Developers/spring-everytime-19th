package com.ceos19.springboot.service;

import com.ceos19.springboot.comment.domain.Comment;
import com.ceos19.springboot.post.domain.Post;
import com.ceos19.springboot.school.domain.School;
import com.ceos19.springboot.users.domain.Users;
import com.ceos19.springboot.comment.repository.CommentRepository;
import com.ceos19.springboot.post.repository.PostRepository;
import com.ceos19.springboot.school.repository.SchoolRepository;
import com.ceos19.springboot.users.repository.UserRepository;
import com.ceos19.springboot.users.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private SchoolRepository schoolRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;

    private School saveSchool;
    private Users saveUser;
    private Post savePost;
    @BeforeEach
    public void setUp() {
        saveSchool = School.builder()
                .schoolName("홍익대학교")
                .dept("컴퓨터공학과")
                .build();
        //saveSchool = schoolRepository.save(school);

        saveUser = Users.builder()
                .university(saveSchool)
                .email("email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("정기민")
                .password("비번")
                .build();
        //saveUser = userRepository.save(user);

        savePost = Post.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글1 내용")
                .imageUrl("image url")
                .title("게시글1 제목")
                .likes(0)
                .user(saveUser)
                .build();
        //savePost = postRepository.save(post1);
    }

    @Test
    @DisplayName("사용자 저장 테스트")
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
        BDDMockito.when(userService.saveUser(user)).thenReturn(user.getUserId());
        Long savedUserId = userService.saveUser(user);
        //then
        Assertions.assertThat(savedUserId).isEqualTo(user.getUserId());
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