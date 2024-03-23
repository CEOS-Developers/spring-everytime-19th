package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Category;
import com.ceos19.springeverytime.domain.Comment;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class CommentRepositoryTest {
    @Autowired CommentRepository commentRepository;
    @Autowired PostRepository postRepository;
    @Autowired UserRepository userRepository;
    @Autowired CategoryRepository categoryRepository;

    User user1, user2;
    Category category;
    Post post1, post2;

    @BeforeEach
    void 테스트_셋업() {
        user1 = createUser("id1");
        user2 = createUser("id2");
        category = createCategory(user1);
        post1 = createPost(user1, category);
        post2 = createPost(user2, category);
    }

    @Test
    @DisplayName("댓글 생성 테스트")
    void 댓글_생성_테스트() {
        // given
        Comment comment = new Comment("댓글1",true, user2, post1);

        // when
        Comment saveComment = commentRepository.save(comment);

        // then
        Assertions.assertThat(saveComment).isSameAs(comment);
        Assertions.assertThat(saveComment.getContent()).isEqualTo("댓글1");
    }

    @Test
    @DisplayName("댓글 단건 조회 테스트")
    void 댓글_단건_조회_테스트() {
        // given
        Comment comment = new Comment("댓글1",true, user2, post1);
        commentRepository.save(comment);

        // when
        Optional<Comment> findComment = commentRepository.findById(comment.getCommentId());

        // then
        Assertions.assertThat(findComment.isPresent()).isEqualTo(true);
        Assertions.assertThat(findComment.get()).isSameAs(comment);
        Assertions.assertThat(findComment.get().getContent()).isEqualTo("댓글1");
    }

    @Test
    @DisplayName("특정 게시글의 모든 댓글 조회 테스트")
    void 특정_게시글의_모든_댓글_조회_테스트() {
        // given
        Comment comment1 = new Comment("댓글1",true, user2, post1);
        Comment comment2 = new Comment("댓글2",true, user1, post1);
        Comment comment3 = new Comment("댓글3",true, user2, post1);
        Comment comment4 = new Comment("댓글4",true, user2, post2);
        Comment comment5 = new Comment("댓글5",true, user1, post2);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);
        commentRepository.save(comment5);

        // when
        List<Comment> commentListOfPost1 = commentRepository.findAllByPost(post1);

        // then
        Assertions.assertThat(commentListOfPost1.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void 댓글_삭제_테스트() {
        // given
        Comment comment1 = new Comment("댓글1",true, user2, post1);
        Comment saveComment = commentRepository.save(comment1);

        // when
        commentRepository.delete(saveComment);

        // then
        Optional<Comment> findComment = commentRepository.findById(comment1.getCommentId());
        Assertions.assertThat(findComment.isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("대댓글 생성 테스트")
    void 대댓글_생성_테스트() {
        // given
        Comment comment1 = new Comment("댓글1",true, user2, post1);
        Comment comment2 = new Comment("댓글2",true, user1, post1);
        comment2.setParentComment(comment1);
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        // when
        Optional<Comment> findComment1 = commentRepository.findById(comment1.getCommentId());
        Optional<Comment> findComment2 = commentRepository.findById(comment2.getCommentId());

        // then
        Assertions.assertThat(findComment1.isPresent()).isEqualTo(true);
        Assertions.assertThat(findComment2.isPresent()).isEqualTo(true);
        Assertions.assertThat(findComment2.get().getParentComment()).isSameAs(comment1);
    }

    private User createUser(String id) {
        User user = new User(
                id,
                "1234",
                "nickname",
                "kim",
                "computer",
                "20",
                "test@example.com");

        return userRepository.save(user);
    }

    private Category createCategory(User manager) {
        Category category = new Category("자유게시판", "자유롭게 이야기 해봐요", manager);
        return categoryRepository.save(category);
    }

    private Post createPost(User author, Category category) {
        Post post = new Post("첫번째 글", "첫번째 글입니다.", true, new Date(), new Date(), author, category);
        return postRepository.save(post);

    }
}
