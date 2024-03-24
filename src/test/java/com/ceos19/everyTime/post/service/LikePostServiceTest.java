package com.ceos19.everyTime.post.service;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.everyTime.community.domain.Community;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.post.domain.LikePost;
import com.ceos19.everyTime.post.domain.Post;
import com.ceos19.everyTime.post.repository.LikePostRepository;
import com.ceos19.everyTime.post.repository.PostRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;

@ExtendWith(MockitoExtension.class)
class LikePostServiceTest {

    @InjectMocks
    LikePostService likePostService;

    @Mock
    PostRepository postRepository;

    @Mock
    LikePostRepository likePostRepository;


    @Test
    void likePost() {
        //좋아요 한 번 누를 경우
        //given
        Member member1 = Member.builder().name("일진우").nickName("dionisos1981").loginId("id").password("password").build();
        Member member2 = Member.builder().name("이진우").nickName("dionisos1982").loginId("id2").password("password2").build();
        ReflectionTestUtils.setField(member1,"id",1L);
        ReflectionTestUtils.setField(member2,"id",2L);

        Community community=Community.builder().member(member1).name("자유게시판").build();
        Post post = Post.builder().title("좋아").contents("너가").isHideNickName(true).isQuestion(false).community(community).member(member1).build();
        ReflectionTestUtils.setField(post,"id",3L);
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        when(likePostRepository.findByMemberIdAndPostId(anyLong(),anyLong())).thenReturn(Optional.empty());



        //when
        likePostService.likePost(member2,1L);

        //then
        assertThat(post.getLikeCount()).isEqualTo(1);

        //좋아요 한번 더 누를 경우

        //given
        LikePost likePost=LikePost.builder().post(post).member(member2).build();
        when(likePostRepository.findByMemberIdAndPostId(anyLong(),anyLong())).thenReturn(Optional.of(likePost));


        //when
        likePostService.likePost(member2,1L);

        //then
        assertThat(post.getLikeCount()).isEqualTo(0);

    }
}