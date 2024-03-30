package com.ceos19.everyTime.community.service;

import com.ceos19.everyTime.community.domain.Community;
import com.ceos19.everyTime.community.dto.request.CommunitySaveRequestDto;
import com.ceos19.everyTime.community.dto.response.CommunityResponseDto;
import com.ceos19.everyTime.community.repository.CommunityRepository;
import com.ceos19.everyTime.member.domain.Member;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;




@ExtendWith(MockitoExtension.class)
public class CommunityServiceTest {

    @Mock
    private CommunityRepository communityRepository;

    @InjectMocks
    private CommunityService communityService;




    @Test
    @DisplayName("게시판 생성 기능이 제대로 되는지 확인 ")
    void saveCommunity() {
        //given
        CommunitySaveRequestDto communitySaveRequestDto = CommunitySaveRequestDto.builder().name("자유게시판").build();
        Member member = Member.builder().name("이진우").nickName("dionisos198").loginId("fddf").password("fdfdf").build();
        when(communityRepository.save(any(Community.class))).thenReturn(toEntity(
            communitySaveRequestDto,member));

        //when
        communityService.saveCommunity(communitySaveRequestDto,member);


        //then
        //...
    }
    private Community toEntity(CommunitySaveRequestDto communitySaveRequestDto,Member member){
        return Community.of(member,communitySaveRequestDto.getName());
    }

    @Test
    @DisplayName("게시판 삭제 기능이 제대로 되는지 확인 ")
    void deleteCommunity() {
        //given
        CommunitySaveRequestDto communitySaveRequestDto = CommunitySaveRequestDto.builder().name("자유게시판").build();
        Member member = Member.builder().name("이진우").nickName("dionisos198").loginId("fddf").password("fdfdf").build();
        //when(communityRepository.save(any(Community.class))).thenReturn(toEntity(communitySaveDto,member));
        when(communityRepository.findById(anyLong())).thenReturn(Optional.of(toEntity(
            communitySaveRequestDto,member)));
        //doNothing().when(communityRepository).delete(any());


        //when
        communityService.deleteCommunity(2L,member);

        //then
        verify(communityRepository,times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("게시판 보여주기 기능 확인")
    void showCommunityList() {
        //when
        List<Community> communityList = new ArrayList<>();
        CommunitySaveRequestDto communitySaveRequestDto1 = CommunitySaveRequestDto.builder().name("자유게시판").build();
        Member member1 = Member.builder().name("이진우1").nickName("dionisos1981").loginId("fddf1").password("fdfdf1").build();
        CommunitySaveRequestDto communitySaveRequestDto2 = CommunitySaveRequestDto.builder().name("비밀게시판").build();
        Member member2 = Member.builder().name("이진우2").nickName("dionisos1982").loginId("fddf2").password("fdfdf2").build();
        communityList.add(toEntity(communitySaveRequestDto1,member1));
        communityList.add(toEntity(communitySaveRequestDto2,member2));
        when(communityRepository.findAll()).thenReturn(communityList);

        //then
        List<CommunityResponseDto> communityListResponseDto=communityService.showCommunityList();

        //
        Assertions.assertThat(communityListResponseDto.size()).isEqualTo(2);
        Assertions.assertThat(communityListResponseDto.get(0).getName()).isEqualTo("자유게시판");
        Assertions.assertThat(communityListResponseDto.get(1).getName()).isEqualTo("비밀게시판");

    }
}