package com.ceos19.everyTime.community.controller;

import com.amazonaws.Response;
import com.ceos19.everyTime.community.dto.request.CommunitySaveRequestDto;
import com.ceos19.everyTime.community.dto.response.CommunityResponseDto;
import com.ceos19.everyTime.community.service.CommunityService;
import com.ceos19.everyTime.error.ErrorCode;
import com.ceos19.everyTime.error.exception.NotFoundException;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.member.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "게시판에 대한 컨트롤러", description = "게시판을 생성, 조회 , 삭제합니다")
@RequestMapping("/community")
public class CommunityController {
    private final CommunityService communityService;
    private final MemberRepository memberRepository;

    @PostMapping("/{memberId}")
    @Operation(summary = "커뮤니티 생성", description = "게시판을 새로 생성합니다.")
    @Parameters({
        @Parameter(name = "memberId",description = "작성자의 ID를 입력해주세요", in = ParameterIn.PATH ,required = true)
    })
    @ApiResponse(responseCode = "201",description = "커뮤니티 생성 완료")
    public ResponseEntity<Void> saveCommunity(@RequestBody @Valid CommunitySaveRequestDto communitySaveRequestDto,@PathVariable Long memberId){
       return ResponseEntity.created(URI.create("/community/"+communityService.saveCommunity(communitySaveRequestDto,findMember(memberId)))).build();
    }

    @DeleteMapping("/{communityId}/{memberId}")
    @Operation(summary = "커뮤니티 삭제", description = "게시판을 삭제합니다. ")
    @Parameters({
        @Parameter(name = "memberId",description = "삭제할 사람의 ID를 입력해주세요", in = ParameterIn.PATH ,required = true),
        @Parameter(name = "communityId",description = "삭제할 community의 ID 값을 입력해주세요", in = ParameterIn.PATH ,required = true)
    })
    @ApiResponse(responseCode = "200",description = "커뮤니티 삭제 완료")
    public  ResponseEntity<Void> deleteCommunity(@PathVariable Long communityId,@PathVariable Long memberId){
        communityService.deleteCommunity(communityId,findMember(memberId));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/list")
    @Operation(summary = "커뮤니티 리스트 조회", description = "게시판의 목록을 볼 수 있습니다.")
    @ApiResponse(responseCode = "200",description = "커뮤니티 리스트 조회 성공")
    public ResponseEntity<List<CommunityResponseDto>> showCommunityList(){
        return ResponseEntity.ok(communityService.showCommunityList());
    }



    private Member findMember(Long memberId){
     return  memberRepository.findById(memberId).orElseThrow(()->new NotFoundException(
            ErrorCode.MESSAGE_NOT_FOUND));
    }




}
