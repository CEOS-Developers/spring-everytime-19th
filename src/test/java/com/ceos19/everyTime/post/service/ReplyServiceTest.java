package com.ceos19.everyTime.post.service;

import com.ceos19.everyTime.community.domain.Community;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.post.domain.Post;
import com.ceos19.everyTime.post.domain.Reply;
import com.ceos19.everyTime.post.dto.request.ReplyCommentSaveRequestDto;
import com.ceos19.everyTime.post.repository.PostRepository;
import com.ceos19.everyTime.post.repository.ReplyRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import org.springframework.test.util.ReflectionTestUtils;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ReplyServiceTest {
    @InjectMocks
    ReplyService replyService;

    @Mock
    PostService postService;

    @Mock
    PostRepository postRepository;

    @Mock
    ReplyRepository replyRepository;



    @Test
    void saveComment() {
        //given
        Member member1 = Member.builder().name("일진우").nickName("dionisos1981").loginId("id1").password("pw1").build();
        Member member2 = Member.builder().name("이진우").nickName("dionisos1982").loginId("id2").password("pw2").build();
        Member member3 = Member.builder().name("삼진우").nickName("dionisos1983").loginId("id3").password("pw3").build();
        Community community = Community.of(member1,"자유게시판");

        Post post = Post.builder().member(member1).community(community).isHideNickName(true).isQuestion(false).title("테스트 title").contents("테스트 컨텐츠").build();
        Reply parentReply = Reply.builder().parent(null).isHideNickName(true).writer("익명1").post(post).contents("낭낭").member(member2).build();
        ReflectionTestUtils.setField(post,"id",1L);
        ReflectionTestUtils.setField(parentReply,"id",1L);
        ReflectionTestUtils.setField(post,"hideNameSequence",1);
        ReflectionTestUtils.setField(post,"replyCount",1);
        ReflectionTestUtils.setField(member1,"id",1L);
        ReflectionTestUtils.setField(member3,"id",3L);

        ReplyCommentSaveRequestDto replyCommentSaveRequestDto = ReplyCommentSaveRequestDto.builder()
                .comment("댓글 두번째")
                    .hideNickName(true)
                        .parentId(1L)
                            .build();



        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(replyRepository.findById(anyLong())).thenReturn(Optional.of(parentReply));
        when(replyRepository.findDefaultNickNameReplyByMemberIdAndPostIdLimitOne(anyLong(),anyLong())).thenReturn(Optional.empty());
        when(postService.makeNextNickNameForHideNickName(any(Post.class))).thenReturn("익명2");



        //when
        Reply savedReply1 = replyService.saveTestComment(1L, replyCommentSaveRequestDto,member3);


        //then
        assertThat(savedReply1.getContents()).isEqualTo("댓글 두번째");
        assertThat(savedReply1.getWriter()).isEqualTo("익명2");
        assertThat(savedReply1.getPost().getReplyCount()).isEqualTo(2);


    }



    @Test
    void deleteComment() {


    }
}