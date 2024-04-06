package com.ceos19.springboot.service;

import com.ceos19.springboot.post.domain.Post;
import com.ceos19.springboot.postlike.domain.PostLike;
import com.ceos19.springboot.postlike.service.PostLikeService;
import com.ceos19.springboot.school.domain.School;
import com.ceos19.springboot.post.service.PostService;
import com.ceos19.springboot.users.domain.Users;
import com.ceos19.springboot.postlike.repository.PostLikeRepository;
import com.ceos19.springboot.post.repository.PostRepository;
import com.ceos19.springboot.school.repository.SchoolRepository;
import com.ceos19.springboot.users.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private PostLikeService postLikeService;
    @Autowired
    private PostRepository postRepository;

    private School saveSchool;
    private Users saveUser;


    @BeforeEach
    public void setUp() {
        School school = School.builder()
                .schoolName("홍익대학교")
                .dept("컴퓨터공학과")
                .build();
        saveSchool = schoolRepository.save(school);

        Users user = Users.builder()
                .university(school)
                .email("email")
                .loginId("아이디")
                .nickname("닉넴")
                .username("정기민")
                .password("비번")
                .build();
        saveUser = userRepository.save(user);
    }

    @Test
    @DisplayName("게시글 조회(페이징 사용)")
    void retrievePostsPaged() throws Exception {
        //given
        Post post1 = Post.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글1 내용")
                .imageUrl("image url")
                .title("게시글1 제목")
                .likes(0)
                .user(saveUser)
                .build();
        Post post2 = Post.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글2 내용")
                .imageUrl("image url")
                .title("게시글2 제목")
                .likes(0)
                .user(saveUser)
                .build();
        Post post3 = Post.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글3 내용")
                .imageUrl("image url")
                .title("게시글3 제목")
                .likes(0)
                .user(saveUser)
                .build();
        Post post4 = Post.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글4 내용")
                .imageUrl("image url")
                .title("게시글4 제목")
                .likes(0)
                .user(saveUser)
                .build();
        Post post5 = Post.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글5 내용")
                .imageUrl("image url")
                .title("게시글5 제목")
                .likes(0)
                .user(saveUser)
                .build();
        Post post6 = Post.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글6 내용")
                .imageUrl("image url")
                .title("게시글6 제목")
                .likes(0)
                .user(saveUser)
                .build();
        Post post7 = Post.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글7 내용")
                .imageUrl("image url")
                .title("게시글7 제목")
                .likes(0)
                .user(saveUser)
                .build();
        Post post8 = Post.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글8 내용")
                .imageUrl("image url")
                .title("게시글8 제목")
                .likes(0)
                .user(saveUser)
                .build();
        postService.savePost(post1);
        postService.savePost(post2);
        postService.savePost(post3);
        postService.savePost(post4);
        postService.savePost(post5);
        postService.savePost(post6);
        postService.savePost(post7);
        postService.savePost(post8);
        //when
        Page<Post> postPage = postService.retrievePostsPaged(1, 4); // 8개의 게시글을 4개씩 페이징
        //then
        Assertions.assertThat(postPage.getTotalPages()).isEqualTo(2); // 총 페이지 개수는 2개
        Assertions.assertThat(postPage.getSize()).isEqualTo(4); // 4개씩 나누었으므로 조회한 postPage는 4개
    }

    @Test
    public void savePostWithImageTest() throws Exception {
        //given
        Post postWithImg = Post.builder()
                .createdAt(LocalDateTime.now())
                .content("사진과 함께 게시글 작성")
                .title("게시글 제목")
                .likes(10)
                .user(saveUser)
                .imageUrl("file:///Users/jeong-kimin/Desktop/%E1%84%87%E1%85%A1%E1%84%90%E1%85%A1%E1%86%BC%E1%84%92%E1%85%AA%E1%84%86%E1%85%A7%E1%86%AB/97235034.png")
                .build();
        //when
        Long savePostId = postService.savePost(postWithImg);
        Optional<Post> findPost = postRepository.findById(savePostId);
        //then
        Assertions.assertThat(savePostId).isEqualTo(1L);
        Assertions.assertThat(findPost.get().getImageUrl()).isEqualTo("file:///Users/jeong-kimin/Desktop/%E1%84%87%E1%85%A1%E1%84%90%E1%85%A1%E1%86%BC%E1%84%92%E1%85%AA%E1%84%86%E1%85%A7%E1%86%AB/97235034.png");
    }

    @Test
    @DisplayName("게시글에 좋아요 누르기 테스트")
    public void postLikeTest() throws Exception {
        //given
        Post post1 = Post.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글 좋아요 누를 내용")
                .imageUrl("image url")
                .title("좋아요 게시글 제목")
                .likes(1000)
                .user(saveUser)
                .build();
        postService.savePost(post1);

        //when
        postService.pressLike(post1.getPostId()); // 사용자가 좋아요를 누르면

        PostLike postLike = PostLike.builder() // 좋아요 누른 정보를 생성 후 디비에 저장
                .post(post1)
                .user(saveUser)
                .build();
        postLikeRepository.save(postLike);

        Post updatedPost = postRepository.findById(post1.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 Post를 찾을 수 없습니다"));

        //then
        Assertions.assertThat(updatedPost.getLikes()).isEqualTo(1001);
    }

    @Test
    public void postUnlikeTest() throws Exception {
        //given
        Post post1 = Post.builder()
                .createdAt(LocalDateTime.now())
                .content("게시글 좋아요 누를 내용")
                .imageUrl("image url")
                .title("좋아요 게시글 제목")
                .likes(1000)
                .user(saveUser)
                .build();
        postService.savePost(post1);
        //when
        postService.pressLike(post1.getPostId()); // 사용자가 좋아요를 누르면

        PostLike postLike = PostLike.builder() // 좋아요 누른 정보를 생성 후 디비에 저장
                .post(post1)
                .user(saveUser)
                .build();
        postLikeRepository.save(postLike);

        postService.unLike(post1);
        postLikeService.unLikePost(postLike);

        Post updatedPost = postRepository.findById(post1.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 Post를 찾을 수 없습니다"));

        //then
        Assertions.assertThat(updatedPost.getLikes()).isEqualTo(1000);
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    public void postDeleteTest() throws Exception {
        //given
        Post post1 = Post.builder()
                .createdAt(LocalDateTime.now())
                .content("삭제할 게시글임")
                .imageUrl("image url")
                .title("삭제될 게시글 제목")
                .likes(4)
                .user(saveUser)
                .build();
        postService.savePost(post1);
        Post post2 = Post.builder()
                .createdAt(LocalDateTime.now())
                .content("삭제안할 게시글임")
                .imageUrl("image url")
                .title("남아있을 게시글 제목")
                .likes(4)
                .user(saveUser)
                .build();
        postService.savePost(post2);
        //when
        postService.deletePost(post1.getPostId());
        //then
    }
}