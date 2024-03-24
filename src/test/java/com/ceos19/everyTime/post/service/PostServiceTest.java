package com.ceos19.everyTime.post.service;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.everyTime.community.domain.Community;
import com.ceos19.everyTime.community.dto.request.CommunitySaveDto;
import com.ceos19.everyTime.community.repository.CommunityRepository;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.member.repository.MemberRepository;
import com.ceos19.everyTime.post.domain.Post;
import com.ceos19.everyTime.post.domain.Reply;
import com.ceos19.everyTime.post.dto.request.PostEditDto;
import com.ceos19.everyTime.post.dto.request.PostSaveDto;
import com.ceos19.everyTime.post.dto.response.PostResponseDto;
import com.ceos19.everyTime.post.repository.PostRepository;
import com.ceos19.everyTime.post.repository.ReplyRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;


@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    PostService postService;

    @Mock
    CommunityRepository communityRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PostRepository postRepository;

    @Mock
    ReplyRepository replyRepository;

    @Test
    @DisplayName("게시물 저장 확인 ")
    void savePost() {
        //given
        PostSaveDto postSaveDto = PostSaveDto.builder()
            .title("여행갔다온 썰 푼다")
            .hideNickName(true)
            .question(true)
            .contents("사실 안갔다옴 ㅋ")
            .multipartFileList(new ArrayList<>())
            .build();
        Community community = Community.builder().name("자유게시판").build();
        when(communityRepository.findById(anyLong())).thenReturn(Optional.of(community));
        ReflectionTestUtils.setField(community,"id",1L);
        Member member=Member
            .builder()
            .name("이진우")
            .nickName("dionisos198")
            .loginId("dfdf")
            .password("dfdffd")
            .build();
        when(postRepository.save(any(Post.class))).thenReturn(toEntity(postSaveDto,member,community));




        //when
        Post findPost = postService.savePost(postSaveDto,1L,member);

        //then
        Assertions.assertThat(findPost.getContents()).isEqualTo("사실 안갔다옴 ㅋ");
        Assertions.assertThat(findPost.getCommunity().getName()).isEqualTo("자유게시판");


    }

    private Post toEntity(PostSaveDto postSaveDto,Member member,Community community){
        return Post.builder()
            .isHideNickName(postSaveDto.isHideNickName())
            .isQuestion(postSaveDto.isQuestion())
            .contents(postSaveDto.getContents())
            .title(postSaveDto.getTitle())
            .member(member)
            .community(community)
            .build();
    }

    @Test
    @DisplayName("게시물 수정 확인")
    void updatePost() {
        //given
        Member member = Member.builder().name("이진우").nickName("dionisos198").loginId("fdfdf").password("asas").build();
        Community community = Community.builder().member(member).name("자유게시판").build();
        Post post = Post.builder().isHideNickName(true).title("좋아").contents("좋아좋아").isQuestion(false).member(member).community(community).build();
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        PostEditDto postEditDto = PostEditDto.builder().title("수정 후 제목").contents("수정 후 내용").hideNickName(false).question(false).multipartFileList(new ArrayList<>()).build();
        ReflectionTestUtils.setField(post,"id",1L);


        //when
        Post updatedPost = postService.updatePost(postEditDto,1L,member);


        //then
        assertThat(updatedPost.getTitle()).isEqualTo("수정 후 제목");
        assertThat(updatedPost.getContents()).isEqualTo("수정 후 내용");

    }

    @Test
    @DisplayName("게시물 삭제 확인")
    void deletePost() {
        //given
        PostSaveDto postSaveDto = PostSaveDto.builder().title("여행갔다온 썰 푼다")
            .hideNickName(true)
            .question(true)
            .contents("사실 안갔다옴 ㅋ")
            .multipartFileList(new ArrayList<>())
            .build();

        Member member = Member.builder().name("이진우").nickName("dionisos198").loginId("fddf").password("fdfdf").build();
        Community community = Community.builder().member(member).name("자유게시판").build();

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(toEntity(postSaveDto,member,community)));



        //when
        postService.deletePost(2L,member);

        //then
        verify(postRepository,times(1)).delete(any(Post.class));
    }

    @Test
    @DisplayName("게시물 리스트 확인")
    void showPostList() {
        //given
        Member member1 = Member.builder().name("이진우1").nickName("dionisos1981").loginId("fddf1").password("fdfdf1").build();
        Member member2 = Member.builder().name("이진우2").nickName("dionisos1982").loginId("fddf2").password("fdfdf2").build();
        Member member3 = Member.builder().name("이진우3").nickName("dionisos1983").loginId("fddf3").password("fdfdf3").build();
        Community community = Community.builder().member(member1).name("자유게시판").build();

        //when
        //postService.showPostList(1L, PageRequest.of(0,2));




    }

    @Test
    @DisplayName("게시물 단건 조회 확인")
    void showDetailsPost() {
        //given
        PostSaveDto postSaveDto = PostSaveDto.builder().title("여행갔다온 썰 푼다")
            .hideNickName(true)
            .question(true)
            .contents("사실 안갔다옴 ㅋ")
            .multipartFileList(new ArrayList<>())
            .build();

        Member postMember = Member.builder().name("이진우").nickName("dionisos198").loginId("fddf").password("fdfdf").build();

        Member replyMember1 = Member.builder().name("일진우").nickName("dionisos1").loginId("fddf1").password("fdfd1").build();
        Member replyMember2 = Member.builder().name("이진우").nickName("dionisos2").loginId("fddf2").password("fdfd2").build();
        Member replyMember3 = Member.builder().name("삼진우").nickName("dionisos3").loginId("fddf3").password("fdfd3").build();

        Community community = Community.builder().member(postMember).name("자유게시판").build();
        Post post = toEntity(postSaveDto,postMember,community);

        ReflectionTestUtils.setField(post,"id",1L);
        when(postRepository.findPostByPostIdWithFetchMemberAndPostImageList(anyLong())).thenReturn(Optional.of(post));

        Reply reply1 = Reply.builder().writer("익명1").post(post).contents("머라는거").member(replyMember1).parent(null).build();
        Reply reply2 = Reply.builder().writer("익명2").post(post).contents("하 참").member(replyMember2).parent(null).build();
        Reply reply3 = Reply.builder().writer("익명3").post(post).contents("그러게 ㅋ").member(replyMember3).parent(reply1).build();
        reply1.getChildList().add(reply3);


        when(replyRepository.findParentReplyByPostIdWithFetchMember(anyLong())).thenReturn(List.of(reply1,reply2));


        //when
        PostResponseDto postResponseDto = postService.showDetailsPost(post.getId());


        //then
        assertThat(postResponseDto.getContents().equals("사실 안갔다옴 ㅋ"));
        assertThat(postResponseDto.getReplyDtoList().size()).isEqualTo(3);
        assertThat(postResponseDto.getReplyDtoList().get(0).getComment()).isEqualTo("머라는거");
        assertThat(postResponseDto.getReplyDtoList().get(1).getComment()).isEqualTo("그러게 ㅋ");
        assertThat(postResponseDto.getReplyDtoList().get(2).getComment()).isEqualTo("하 참");


    }
}