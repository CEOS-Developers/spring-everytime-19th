package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.common.EntityGenerator;
import com.ceos19.springeverytime.domain.category.domain.Category;
import com.ceos19.springeverytime.domain.comment.repository.CommentRepository;
import com.ceos19.springeverytime.domain.comment.domain.Comment;
import com.ceos19.springeverytime.domain.post.domain.Post;
import com.ceos19.springeverytime.domain.category.repository.CategoryRepository;
import com.ceos19.springeverytime.domain.post.repository.PostRepository;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class CommentRepositoryTest {
    @Autowired EntityManager em;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;

    User user1, user2;
    Category category;
    Post post1, post2;

    @BeforeEach
    void 테스트_셋업() {
        user1 = userRepository.save(EntityGenerator.generateUser("id1"));
        user2 = userRepository.save(EntityGenerator.generateUser("id2"));
        category = categoryRepository.save(EntityGenerator.generateCategory(user1));
        post1 = postRepository.save(EntityGenerator.generatePost(user1, category));
        post2 = postRepository.save(EntityGenerator.generatePost(user2, category));
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
        post1.addComment(user2, "댓글1", true);
        em.flush();
        em.clear();

        // when
        Post post = postRepository.findById(post1.getPostId()).get();
        System.out.println("--------------");
        Comment comment = post.getComments().get(0);
        Optional<Comment> findComment = commentRepository.findById(comment.getCommentId());

        // then
        Assertions.assertThat(findComment.isPresent()).isTrue();
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

//    @Test
//    @DisplayName("대댓글 생성 테스트")
//    void 대댓글_생성_테스트() {
//        // given
//        Comment parent = post1.addComment(user2, "댓글1", true);
//        Comment child = parent.addReply(user1, "댓글", true);
//
//        em.flush();
//        em.clear();
//
//        // when
//        Optional<Comment> findComment1 = commentRepository.findById(parent.getCommentId());
//        Optional<Comment> findComment2 = commentRepository.findById(child.getCommentId());
//
//        // then
//        Assertions.assertThat(findComment1.isPresent()).isEqualTo(true);
//        Assertions.assertThat(findComment2.isPresent()).isEqualTo(true);
//        Assertions.assertThat(findComment2.get().getParentComment()).isSameAs(findComment1.get());
//    }

//    @Test
//    @DisplayName("부모 댓글 삭제 테스트")
//    void 부모댓글_삭제_테스트() {
//        // given
//        Comment parent = post1.addComment(user2, "댓글1", true);
//        parent.addReply(user1, "댓글2", true);
//        parent.addReply(user1, "댓글3", true);
//        em.flush();
//
//        // when
//        for (int i = 0; i < 2; i++) {
//            parent.getChildComments().get(i).setParentComment(null);
//        }
//        em.flush();
//        em.clear();
//        // 연관관계가 있는 child 가 1차 캐시에 있는 상태에서는 parent를 지워도 쿼리가 안나감.
//        // https://velog.io/@jkijki12/JPA-Entity%EA%B0%80-delete%EA%B0%80-%EC%95%88%EB%90%9C%EB%8B%A4 참고
//
//        System.out.println("--------------------------");
//        commentRepository.delete(commentRepository.findById(parent.getCommentId()).get());
//        em.flush();
//        em.clear();
//        System.out.println("--------------------------");
//
//        // then
//        Assertions.assertThat(commentRepository.findAllByPost(post1).size()).isEqualTo(2);
//    }
}
