package com.ceos19.everyTime.jjokji.service;

import com.ceos19.everyTime.jjokji.domain.Jjokji;
import com.ceos19.everyTime.jjokji.domain.JjokjiRoom;
import com.ceos19.everyTime.jjokji.dto.response.JjokjiLatestResponseDto;
import com.ceos19.everyTime.jjokji.dto.response.JjokjiResponseDto;
import com.ceos19.everyTime.jjokji.repository.JjokjiRepository;
import com.ceos19.everyTime.jjokji.repository.JjokjiRoomRepository;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import static org.mockito.ArgumentMatchers.anyLong;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class JjokjiServiceTest {

    @Mock
    JjokjiRepository jjokjiRepository;

    @Mock
    JjokjiRoomRepository jjokjiRoomRepository;

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    JjokjiService jjokjiService;


    @Test
    @DisplayName("각 쪽지방을 대표하는 최근 쪽지 확인")
    void showJjokjiRoomByLatestJjokji() {
        //given
        Member member1=Member.builder().loginId("dionisos198").name("이진우").nickName("dionisos198").password("ddd").build();
        Member member2 = Member.builder().loginId("dionisos1982").name("이진우2").nickName("dionisos1982").password("ddd2").build();
        Member member3 = Member.builder().loginId("dionisos1983").name("이진우3").nickName("dionisos1983").password("ddd3").build();
        JjokjiRoom room1 = makeJjokjiRoom(member1,member2);
        JjokjiRoom room2 = makeJjokjiRoom(member2,member3);
        ReflectionTestUtils.setField(room1,"id",1L);
        ReflectionTestUtils.setField(room2,"id",2L);
        ReflectionTestUtils.setField(member2,"id",3L);
        when(jjokjiRoomRepository.findJjokjiRoomByMemberId(anyLong())).thenReturn(List.of(room1,room2));
        when(jjokjiRepository.findLatestJjokjiByJjokjiRoomId(1L)).thenReturn(Optional.of(makeJjokji("앙녕",member1,member2)));
        when(jjokjiRepository.findLatestJjokjiByJjokjiRoomId(2L)).thenReturn(Optional.of(makeJjokji("빵",member2,member3)));

        //when
        List<JjokjiLatestResponseDto> jjokjiLatestResponses = jjokjiService.showJjokjiRoomByLatestJjokji(member2);

        //return
        Assertions.assertThat(jjokjiLatestResponses.size()).isEqualTo(2);
        Assertions.assertThat(jjokjiLatestResponses.get(0).getMessage()).isEqualTo("앙녕");
        Assertions.assertThat(jjokjiLatestResponses.get(1).getMessage()).isEqualTo("빵");

    }

    @Test
    @DisplayName("메시지 전송확인")
    void sendMessage() {
        //given
        Member sender=Member.builder().loginId("dionisos198").name("이진우").nickName("dionisos198").password("ddd").build();
        Member receiver = Member.builder().loginId("dionisos1982").name("이진우2").nickName("dionisos1982").password("ddd2").build();
        when(memberRepository.findById(any())).thenReturn(Optional.of(receiver));
        ReflectionTestUtils.setField(sender,"id",1L);
        when(jjokjiRoomRepository.findByJjokjiRoomByParticipant(anyLong(),anyLong())).thenReturn(Optional.of(makeJjokjiRoom(sender,receiver)));
        when(jjokjiRepository.save(any(Jjokji.class))).thenReturn(makeJjokji("안녕하세요",sender,receiver));



        //when
        Jjokji result = jjokjiService.sendMessage("안녕하세요",sender,1L);


        //then
        Assertions.assertThat(result.getMessage()).isEqualTo("안녕하세요");
        Assertions.assertThat(result.getJjokjiRoom().getFirstSender().getNickName()).isEqualTo("dionisos198");
        Assertions.assertThat(result.getJjokjiRoom().getFirstReceiver().getNickName()).isEqualTo("dionisos1982");
        Assertions.assertThat(result.getMember().getNickName()).isEqualTo("dionisos198");

    }

    private JjokjiRoom makeJjokjiRoom(Member sender,Member receiver){
        return JjokjiRoom.builder().firstReceiver(receiver).firstSender(sender).build();
    }

    private Jjokji makeJjokji(String message, Member sender,Member receiver){
        return Jjokji.builder().jjokjiRoom(makeJjokjiRoom(sender,receiver))
            .member(sender)
            .message(message)
            .build();
    }

    @Test
    @DisplayName("한 방에서의 채팅 내역 확인")
    void chatListInOneRoom() {
        //given
        Member sender=Member.builder().loginId("dionisos198").name("이진우").nickName("dionisos198").password("ddd").build();
        Member receiver = Member.builder().loginId("dionisos1982").name("이진우2").nickName("dionisos1982").password("ddd2").build();
        when(jjokjiRepository.findJjokjisByJjokjiRoom_Id(anyLong())).thenReturn(
            List.of(makeJjokji("안녕?",sender,receiver),makeJjokji("뭐라는거야",receiver,sender)));

        //when
        List<JjokjiResponseDto> jjokjiResponsDtos = jjokjiService.ChatListInOneRoom(1L);

        //then
        Assertions.assertThat(jjokjiResponsDtos).size().isEqualTo(2);
        Assertions.assertThat(jjokjiResponsDtos.get(0).getMessage()).isEqualTo("안녕?");
        Assertions.assertThat(jjokjiResponsDtos.get(1).getMessage()).isEqualTo("뭐라는거야");
    }
}