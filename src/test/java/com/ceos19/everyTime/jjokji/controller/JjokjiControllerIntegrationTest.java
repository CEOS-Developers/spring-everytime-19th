package com.ceos19.everyTime.jjokji.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ceos19.everyTime.jjokji.domain.JjokjiRoom;
import com.ceos19.everyTime.jjokji.dto.request.MessageRequestDto;
import com.ceos19.everyTime.jjokji.repository.JjokjiRoomRepository;
import com.ceos19.everyTime.jjokji.service.JjokjiService;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.member.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class JjokjiControllerIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JjokjiService jjokjiService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JjokjiRoomRepository jjokjiRoomRepository;


    private Member getMember(int i){
        Member member =Member.builder()
            .loginId("dionisos198"+i)
            .name("jinuYi"+i)
            .password("haruka198"+i)
            .nickName("kkkk"+i)
            .build();

        memberRepository.save(member);

        return member;
    }


    @Autowired
    private MockMvc mockMvc;


    @Test
    void showJjokjiRoomList() throws Exception {
        //given
        Member firstSender = getMember(1);
        Member firstReciever = getMember(2);
        Member thirdMember = getMember(3);


        jjokjiService.sendMessage("안녕하세요",firstSender, firstReciever.getId());
        jjokjiService.sendMessage("안녕히가세요",firstReciever, firstSender.getId());
        jjokjiService.sendMessage("누구세요",thirdMember, firstSender.getId());

        //when
        ResultActions resultActions = mockMvc.perform(get("/"+firstSender.getId()+"/jjokjiRoomList")
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print());



        //then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.data.[0].message").value(equalTo("누구세요")))
            .andExpect(jsonPath("$.data.[1].message").value(equalTo("안녕히가세요")));


    }

    @Test
    void sendMessage() throws Exception {
        //given
        Member firstSender = getMember(1);
        Member firstReciever = getMember(2);
        MessageRequestDto messageRequestDto = MessageRequestDto
            .builder()
            .message("족보구해요")
            .senderId(firstSender.getId())
            .receiverId(firstReciever.getId())
            .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageRequestDto)));

        //then
       resultActions.andExpect(status().isCreated());



    }

    @Test
    void showRoomMessage() throws Exception {
        //given
        Member firstSender = getMember(1);
        Member firstReciever = getMember(2);
        Member thirdMember = getMember(3);


        jjokjiService.sendMessage("안녕하세요",firstSender, firstReciever.getId());
        jjokjiService.sendMessage("안녕히가세요",firstReciever, firstSender.getId());
        jjokjiService.sendMessage("누구세요",thirdMember, firstSender.getId());

        Long chatRoomId = jjokjiService.showJjokjiRoomByLatestJjokji(firstSender).get(1).getChatRoomId();


        //when
        ResultActions resultActions = mockMvc.perform(get("/"+chatRoomId+"/message")
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print());

        //then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.data.[0].message").value(equalTo("안녕하세요")))
            .andExpect(jsonPath("$.data.[1].message").value(equalTo("안녕히가세요")));
    }
}