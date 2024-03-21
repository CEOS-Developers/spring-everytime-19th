package com.ceos19.everytime.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PostTest {

    @ParameterizedTest
    @CsvSource(value = {"true, 익명", "false, nickname"})
    void isAnounymous가_true이면_작성자의_닉네임은_익명이_된다(boolean isAnonymous, String expected) {
        // given
        final User user = User.builder()
                .nickname("nickname")
                .build();
        final Board board = Board.builder()
                .name("boardName")
                .build();
        final Post post = Post.builder()
                .writer(user)
                .board(board)
                .isAnonymous(isAnonymous)
                .build();

        // when
        final String nickname = post.getWriterNickname();

        // then
        assertThat(nickname).isEqualTo(expected);
    }

    @Test
    void 좋아요_수가_0보다_같거나_작은_게시글의_좋아요_수를_감소시키면_예외가_발생한다() {
        // given
        final User user = User.builder()
                .nickname("nickname")
                .build();
        final Post post = Post.builder()
                .writer(user)
                .isAnonymous(false)
                .build();

        // when & then
        assertThatThrownBy(() -> post.decreaseLikeNumber())
                .isInstanceOf(IllegalArgumentException.class);
    }
}
