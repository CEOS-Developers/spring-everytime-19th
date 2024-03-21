package com.ceos19.everytime.service;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
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

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.dto.request.BoardPostsRequestDto;
import com.ceos19.everytime.dto.request.PostCreateRequestDto;
import com.ceos19.everytime.dto.response.BoardPostsResponseDto;
import com.ceos19.everytime.dto.response.PostResponseDto;
import com.ceos19.everytime.repository.BoardRepository;
import com.ceos19.everytime.repository.PostRepository;
import com.ceos19.everytime.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private PostService postService;

    private User user;
    private Board board;
    private Post post;

    @BeforeEach
    void setUp() {
        user = User.builder()
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
                .userId(1L)
                .build();

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.of(board));

        // when & then
        postService.createPost(request);

        // then
        verify(postRepository).save(post);
    }

    @Test
    void 게시글을_조회한다() {
        // given
        given(postRepository.findById(anyLong()))
                .willReturn(Optional.of(post));

        // when
        final PostResponseDto response = postService.getPost(1L);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.title()).isEqualTo("title");
            softly.assertThat(response.content()).isEqualTo("안녕하세요~");
            softly.assertThat(response.username()).isEqualTo("익명");
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
        final BoardPostsResponseDto response = postService.getPosts(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.responses().size()).isEqualTo(3);
            softly.assertThat(response.responses().get(0)).isEqualTo(post.toResponseDto());
            softly.assertThat(response.responses().get(1)).isEqualTo(post2.toResponseDto());
            softly.assertThat(response.responses().get(2)).isEqualTo(post3.toResponseDto());
        });
    }
}
