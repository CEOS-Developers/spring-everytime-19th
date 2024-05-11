package com.ceos19.everytime.post.service;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ceos19.everytime.board.domain.Board;
import com.ceos19.everytime.board.dto.request.BoardPostsRequestDto;
import com.ceos19.everytime.board.dto.response.BoardPostsResponseDto;
import com.ceos19.everytime.board.repository.BoardRepository;
import com.ceos19.everytime.comment.domain.Comment;
import com.ceos19.everytime.comment.repository.CommentRepository;
import com.ceos19.everytime.post.domain.Post;
import com.ceos19.everytime.post.dto.request.PostCreateRequestDto;
import com.ceos19.everytime.post.dto.response.PostResponseDto;
import com.ceos19.everytime.post.repository.PostRepository;
import com.ceos19.everytime.user.domain.User;
import com.ceos19.everytime.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private PostService postService;

    private User user;
    private Board board;
    private Post post;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("username")
                .nickname("nickname")
                .build();
        board = Board.builder()
                .name("boardName")
                .build();
        post = Post.builder()
                .title("title")
                .content("안녕하세요~")
                .board(board)
                .isAnonymous(true)
                .writer(user)
                .build();
    }

    @Test
    void 게시글_생성_테스트() {
        // given
        final PostCreateRequestDto request = PostCreateRequestDto.builder()
                .boardId(1L)
                .title("title")
                .content("안녕하세요~")
                .isAnonymous(true)
                .build();

        given(userRepository.findByUsername(anyString()))
                .willReturn(Optional.of(user));
        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.of(board));

        // when
        postService.createPost(request, user.getUsername());

        // then
        verify(postRepository).save(post);
    }

    @Test
    void 게시글을_조회한다() {
        // given
        given(postRepository.findById(anyLong()))
                .willReturn(Optional.of(post));

        final Comment comment1 = new Comment("content1", true, user, post, null);
        final Comment comment2 = new Comment("content2", true, user, post, null);
        final Comment comment3 = new Comment("content3", true, user, post, null);

        given(commentRepository.findByPost(any()))
                .willReturn(List.of(comment1, comment2, comment3));

        // when
        final PostResponseDto response = postService.getPost(1L);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.title()).isEqualTo("title");
            softly.assertThat(response.content()).isEqualTo("안녕하세요~");
            softly.assertThat(response.username()).isEqualTo("익명");
            softly.assertThat(response.comments().size()).isEqualTo(3);
        });
    }

    @Test
    void 게시판에_해당하는_모든_게시글을_조회한다() {
        // given
        final BoardPostsRequestDto request = new BoardPostsRequestDto(1L);
        final Post post2 = Post.builder()
                .title("title2")
                .writer(user)
                .content("안녕하세요~2")
                .board(board)
                .isAnonymous(true)
                .build();
        final Post post3 = Post.builder()
                .title("title3")
                .writer(user)
                .content("안녕하세요~3")
                .board(board)
                .isAnonymous(true)
                .build();

        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.of(board));
        given(postRepository.findAllFetchJoin(board))
                .willReturn(List.of(post, post2, post3));

        // when
        final List<BoardPostsResponseDto> response = postService.getPosts(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.size()).isEqualTo(3);
            softly.assertThat(response.get(0)).isEqualTo(BoardPostsResponseDto.from(post));
            softly.assertThat(response.get(1)).isEqualTo(BoardPostsResponseDto.from(post2));
            softly.assertThat(response.get(2)).isEqualTo(BoardPostsResponseDto.from(post3));
        });
    }
}
