package com.ceos19.everytime.service;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.domain.User;
import com.ceos19.everytime.dto.request.PostRequestDto;
import com.ceos19.everytime.dto.response.PostResponseDto;
import com.ceos19.everytime.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void 게시글을_조회한다() {
        // given
        final PostRequestDto request = new PostRequestDto(1L);
        final User user = User.builder()
                .nickname("hello")
                .build();
        final Board board = Board.builder()
                .name("boardName")
                .build();
        final Post post = Post.builder()
                .id(1L)
                .title("title")
                .user(user)
                .content("안녕하세요~")
                .board(board)
                .isAnonymous(true)
                .build();

        given(postRepository.findById(anyLong()))
                .willReturn(Optional.of(post));

        // when
        final PostResponseDto response = postService.getPost(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.title()).isEqualTo("title");
            softly.assertThat(response.content()).isEqualTo("안녕하세요~");
            softly.assertThat(response.username()).isEqualTo("익명");
        });
    }
}
