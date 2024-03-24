package com.ceos19.everyTime.community.service;

import com.ceos19.everyTime.community.domain.Community;
import com.ceos19.everyTime.community.dto.request.CommunitySaveDto;
import com.ceos19.everyTime.community.dto.response.CommunityDto;
import com.ceos19.everyTime.community.dto.response.CommunityListResponseDto;
import com.ceos19.everyTime.community.repository.CommunityRepository;
import com.ceos19.everyTime.error.ErrorCode;
import com.ceos19.everyTime.error.exception.NotFoundException;
import com.ceos19.everyTime.member.domain.Member;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.results.NoMoreOutputsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;

    //커뮤니티 생성 및 저장

    @Transactional
    public void saveCommunity(CommunitySaveDto communitySaveDto, Member currentMember){
        Community community=Community
            .builder()
            .name(communitySaveDto.getName())
            .member(currentMember)
            .build();

        communityRepository.save(community);
    }

    //커뮤니티 제거 메서드
    @Transactional
    public void deleteCommunity(Long communityId,Member currentMember){

        Community community = communityRepository.findById(communityId).orElseThrow(()->new NotFoundException(
            ErrorCode.MESSAGE_NOT_FOUND));

        //커뮤니티를 생성한 사람의 ID 와 현재 로그인한 Member 의 ID가 다를 경우 예외
        if(community.getMember().getId()!= currentMember.getId()){
            throw new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND);
        }

        communityRepository.deleteById(communityId);
    }

    //커뮤니티 리스트를 반환.
    public CommunityListResponseDto showCommunityList(){
        List<CommunityDto> communityDtoList = communityRepository.findAll().stream().map(CommunityDto::new).collect(
            Collectors.toList());

        return new CommunityListResponseDto(communityDtoList);
    }

}
