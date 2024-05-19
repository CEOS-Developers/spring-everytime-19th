//package com.ceos19.springboot.service;
//
//import com.ceos19.springboot.comment.domain.Comment;
//import com.ceos19.springboot.comment.service.CommentService;
//import com.ceos19.springboot.post.domain.Post;
//import com.ceos19.springboot.school.domain.School;
//import com.ceos19.springboot.post.service.PostService;
//import com.ceos19.springboot.users.domain.Users;
//import com.ceos19.springboot.comment.repository.CommentRepository;
//import com.ceos19.springboot.post.repository.PostRepository;
//import com.ceos19.springboot.school.repository.SchoolRepository;
//import com.ceos19.springboot.users.repository.UserRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//
//@SpringBootTest
//class CommentServiceTest {
//    @Autowired
//    private CommentService commentService;
//    @Autowired
//    private CommentRepository commentRepository;
//    @Autowired
//    private SchoolRepository schoolRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private PostRepository postRepository;
//
//    @Autowired
//    private PostService postService;
//
//    private School saveSchool;
//    private Users saveUser;
//    private Post savePost;
//
//    @BeforeEach
//    public void setUp() {
//        School school = School.builder()
//                .schoolName("홍익대학교")
//                .dept("컴퓨터공학과")
//                .build();
//        saveSchool = schoolRepository.save(school);
//
//        Users user = Users.builder()
//                .university(school)
//                .email("email")
//                .loginId("아이디")
//                .nickname("닉넴")
//                .username("정기민")
//                .password("비번")
//                .build();
//        saveUser = userRepository.save(user);
//
//        Post post1 = Post.builder()
//                .createdAt(LocalDateTime.now())
//                .content("게시글1 내용")
//                .imageUrl("image url")
//                .title("게시글1 제목")
//                .likes(0)
//                .user(saveUser)
//                .build();
//        savePost = postRepository.save(post1);
//    }
//    @Test
//    @DisplayName("댓글 작성하기")
//    public void saveCommentTest() throws Exception {
//        //given
//        Comment comment = Comment.builder()
//                .content("댓글 내용ㅇ오오옹ㅇ")
//                .user(saveUser)
//                .post(savePost)
//                .build();
//        Comment comment2 = Comment.builder()
//                .content("댓글 내용ㅇ오오옹ㅇ")
//                .user(saveUser)
//                .post(savePost)
//                .build();
//        Comment comment3 = Comment.builder()
//                .content("댓글 내용ㅇ오오옹ㅇ")
//                .user(saveUser)
//                .post(savePost)
//                .build();
//        //when
////        Long saveComment = commentService.saveComment(comment);
////        commentService.saveComment(comment2);
////        commentService.saveComment(comment3);
////        Comment findComment = commentRepository.findById(saveComment)
////                .orElseThrow(() -> new EntityNotFoundException("해당 ID를 가진 댓글이 없습니다"));
//        //postService.deletePost(savePost);
//        //then
////        Assertions.assertThat(findComment.getContent()).isEqualTo("댓글 내용ㅇ오오옹ㅇ");
//    }
//
//    @Test
//    @DisplayName("대댓글 작성하기")
//    public void childCommentTest() throws Exception {
//        //given
//        Comment parentComment = Comment.builder()
//                .content("부모 댓글")
//                .user(saveUser)
//                .post(savePost)
//                .build();
//
//        Comment childComment = Comment.builder()
//                .content("대댓글")
//                .user(saveUser)
//                .post(savePost)
//                .build();
//        //when
////        commentService.saveComment(parentComment);
////        commentService.saveComment(childComment); // 디비에 댓글을 저장
////
////        commentService.setChildComment(parentComment,childComment);
////
////        //then
////        List<Comment> children = parentComment.getChildren();
////        for (Comment child : children) {
////            Assertions.assertThat(child.getParent()).isEqualTo(parentComment);
////        }
//    }
//}