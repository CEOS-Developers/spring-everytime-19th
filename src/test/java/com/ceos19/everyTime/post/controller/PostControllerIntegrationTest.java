package com.ceos19.everyTime.post.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ceos19.everyTime.community.domain.Community;
import com.ceos19.everyTime.community.repository.CommunityRepository;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.member.repository.MemberRepository;
import com.ceos19.everyTime.post.domain.Post;
import com.ceos19.everyTime.post.domain.Reply;
import com.ceos19.everyTime.post.dto.request.PostEditRequestDto;
import com.ceos19.everyTime.post.dto.request.PostSaveRequestDto;
import com.ceos19.everyTime.post.dto.request.ReplyCommentSaveRequestDto;
import com.ceos19.everyTime.post.repository.PostRepository;
import com.ceos19.everyTime.post.repository.ReplyRepository;
import com.ceos19.everyTime.post.service.ReplyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class PostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private ObjectMapper objectMapper;


    private Member getMember(int seq){
        Member member = Member.builder()
            .loginId("dionisos198"+seq)
            .password("fff"+seq)
            .name("jinu Yi"+seq)
            .nickName("kkkkk"+seq)
            .build();

        memberRepository.save(member);

        return member;
    }

    private Community getCommunity(){
        Community community=Community.of(getMember(7),"자유게시판");
        communityRepository.save(community);
        return community;
    }

    private Post getPost(Member member,Community community,int i){
        Post post =Post.builder()
            .title("하이용 헤헤"+i)
            .contents("집에가자용"+i)
            .isQuestion(false)
            .isHideNickName(true)
            .community(community)
            .member(member)
            .build();

        postRepository.save(post);

        return post;
    }

    @Test
    void savePost() throws Exception{
        //given
        PostSaveRequestDto postSaveRequestDto=PostSaveRequestDto.builder()
            .title("하이용헤헤")
            .contents("집에가자용")
            .hideNickName(true)
            .question(false)
            .build();
        Member member1 = getMember(1);
        Community community =getCommunity();

        //when
        ResultActions resultActions = mockMvc.perform(post("/post/"+community.getId()+"/"+member1.getId())
            .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("title",postSaveRequestDto.getTitle())
                .param("contents",postSaveRequestDto.getContents())
                .param("hideNickName", String.valueOf(postSaveRequestDto.isHideNickName()))
                .param("question",String.valueOf(postSaveRequestDto.isHideNickName()))
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print());

        //then

        resultActions
            .andExpect(status().isCreated());



    }

    @Test
    void changePost() throws Exception {
        //given
        PostEditRequestDto postEditRequestDto=PostEditRequestDto.builder()
            .title("수정후 하이용헤헤")
            .contents("수정후 집에가자용")
            .hideNickName(true)
            .question(false)
            .build();
        Member member =getMember(3);
        Community community = getCommunity();
        Post post = getPost(member,community,7);

        //when
        ResultActions resultActions = mockMvc.perform(patch("/post/"+post.getId()+"/"+member.getId())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("title",postEditRequestDto.getTitle())
                .param("contents",postEditRequestDto.getContents())
                .param("hideNickName", String.valueOf(postEditRequestDto.isHideNickName()))
                .param("question",String.valueOf(postEditRequestDto.isHideNickName()))
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print());


        //then
        resultActions
            .andExpect(status().isOk());
    }

    @Test
    void deletePost() throws Exception{
        //given
        Member member = getMember(1);
        Community community = getCommunity();
        Post post = getPost(member,community,7);



        ResultActions resultActions = mockMvc.perform(delete("/post/"+post.getId()+"/"+member.getId()));


        resultActions.andExpect(status().isOk());

    }

    @Test
    void 다른_사용자가_삭제_시도시() throws Exception{
        //given
        Member member = getMember(1);
        Member member2 = getMember(2);
        Community community = getCommunity();
        Post post = getPost(member,community,7);



        //when
        ResultActions resultActions = mockMvc.perform(delete("/post/"+post.getId()+"/"+member2.getId()));


        //then
        resultActions.andExpect(status().is4xxClientError());

    }

    @Test
    void showPostList() throws Exception {
        //given
        Member member1 = getMember(1);
        Member member2 = getMember(2);
        Member member3 = getMember(3);

        Community community = getCommunity();
        Post post1 = getPost(member1,community,1);
        Post post2 = getPost(member2,community,2);
        Post post3 = getPost(member3,community,3);



        //when
        ResultActions resultActions = mockMvc.perform(get("/post/"+community.getId()+"/list?size=2"));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(print());


    }

    @Test
    void showPost() throws Exception {
        //given
        Member member1 = getMember(1);
        Member member2 = getMember(2);
        Member member3 = getMember(3);
        Community community = getCommunity();
        Post post1 = getPost(member1,community,1);
        ReplyCommentSaveRequestDto replyCommentSaveRequestDto1=ReplyCommentSaveRequestDto.builder()
                .comment("답글1")
                    .hideNickName(true)
                        .parentId(null).build();
        replyService.saveComment(post1.getId(),replyCommentSaveRequestDto1,member2);



        //when
        ResultActions resultActions = mockMvc.perform(get("/post/"+post1.getId()).accept(MediaType.APPLICATION_JSON));


        //then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.data.title",equalTo("하이용 헤헤1")));
    }

    @Test
    void likePost() throws Exception {
        //given
        Member member1 = getMember(1);
        Member member2 = getMember(2);
        Member member3 = getMember(3);
        Community community = getCommunity();
        Post post1 = getPost(member1,community,1);

        //when
        ResultActions resultActions = mockMvc.perform(post("/post/like/"+post1.getId()+"/"+member2.getId()));

        //then

        resultActions.andExpect(status().isOk()).andDo(print());
    }
}