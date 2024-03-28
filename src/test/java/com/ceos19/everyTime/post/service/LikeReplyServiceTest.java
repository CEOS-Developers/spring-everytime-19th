package com.ceos19.everyTime.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.ceos19.everyTime.community.domain.Community;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.post.domain.LikePost;
import com.ceos19.everyTime.post.domain.LikeReply;
import com.ceos19.everyTime.post.domain.Post;
import com.ceos19.everyTime.post.domain.Reply;
import com.ceos19.everyTime.post.repository.LikePostRepository;
import com.ceos19.everyTime.post.repository.LikeReplyRepository;
import com.ceos19.everyTime.post.repository.PostRepository;
import com.ceos19.everyTime.post.repository.ReplyRepository;
import java.sql.Ref;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class LikeReplyServiceTest {

    @InjectMocks
    LikeReplyService likeReplyService;

    @Mock
    ReplyRepository replyRepository;

    @Mock
    LikeReplyRepository likeReplyRepository;


    @Test
    void likeReply() {
        Member member1 = Member.builder().name("일진우").nickName("dionisos1981").loginId("id").password("password").build();
        Member member2 = Member.builder().name("이진우").nickName("dionisos1982").loginId("id2").password("password2").build();
        ReflectionTestUtils.setField(member1,"id",1L);
        ReflectionTestUtils.setField(member2,"id",2L);

        Community community=Community.builder().member(member1).name("자유게시판").build();
        Post post = Post.builder().title("좋아").contents("너가").isHideNickName(true).isQuestion(false).community(community).member(member1).build();
        ReflectionTestUtils.setField(post,"id",3L);


        Reply reply1 =Reply.builder().member(member1).isHideNickName(true).parent(null).contents("답글").build();
        ReflectionTestUtils.setField(reply1,"id",4L);


        when(replyRepository.findById(anyLong())).thenReturn(Optional.of(reply1));

        when(likeReplyRepository.findByMemberIdAndReplyId(anyLong(),anyLong())).thenReturn(Optional.empty());



        //when
        likeReplyService.likeReply(member2,1L);

        //then
        assertThat(reply1.getLikeCount()).isEqualTo(1);

        //좋아요 한번 더 누를 경우

        //given
        LikeReply likeReply=LikeReply.builder().reply(reply1).member(member2).build();
        when(likeReplyRepository.findByMemberIdAndReplyId(anyLong(),anyLong())).thenReturn(Optional.of(likeReply));


        //when
        likeReplyService.likeReply(member2,1L);

        //then
        assertThat(post.getLikeCount()).isEqualTo(0);
    }
}