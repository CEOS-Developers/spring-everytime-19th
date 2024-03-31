package com.ceos19.everyTime.jjokji.controller;

import com.ceos19.everyTime.common.ApiBaseResponse;
import com.ceos19.everyTime.error.ErrorCode;
import com.ceos19.everyTime.error.exception.NotFoundException;
import com.ceos19.everyTime.jjokji.dto.request.MessageRequestDto;
import com.ceos19.everyTime.jjokji.dto.response.JjokjiLatestResponseDto;
import com.ceos19.everyTime.jjokji.dto.response.JjokjiResponseDto;
import com.ceos19.everyTime.jjokji.repository.JjokjiRepository;
import com.ceos19.everyTime.jjokji.service.JjokjiService;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.member.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "쪽지에 대한 컨트롤러", description = "쪽지를 생성 및 조회합니다. ")
public class JjokjiController {
    private final JjokjiService jjokjiService;
    private final MemberRepository memberRepository;

    @GetMapping("/{memberId}/jjokjiRoomList")
    @Operation(summary = "쪽지방 조회", description = "지금까지 받은 쪽지 보낸 쪽지 등으로 이루어진 쪽지방의 리스트를 보여줍니다(가장 최신 쪽지 내용을 리턴)")
    @ApiResponse(responseCode = "200",description = "커뮤니티 조회 완료")
    @Parameters({
        @Parameter(name = "memberId",description = "현재 로그인한 사용자의 ID(현재는 path 값)", in = ParameterIn.PATH ,required = true)
    })
    public ResponseEntity<ApiBaseResponse<List<JjokjiLatestResponseDto>>> showJjokjiRoomList(@PathVariable("memberId") final Long memberId){
        return ResponseEntity.ok(ApiBaseResponse.createSuccess(jjokjiService.showJjokjiRoomByLatestJjokji(findById(memberId))));

    }

    @PostMapping("/message")
    @Operation(summary = "메시지 보내기", description = "쪽지방이 없으면 새로 생성하며 메시지를 보내고 그렇지 않은 경우 걍 메시지 보내기")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",description = "메시지 성공적 전송"),
        @ApiResponse(responseCode = "404",description = "자원 식별 불가")
    })
    public ResponseEntity<Void>  sendMessage(@RequestBody @Valid final MessageRequestDto messageRequestDto){
        jjokjiService.sendMessage(messageRequestDto.getMessage(),findById(messageRequestDto.getSenderId()),
            messageRequestDto.getReceiverId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{roomId}/message")
    @Operation(summary = "한 방에서 메시지 조회", description = "한 쪽지방에서 메시지 조회를 확인합니다")
    @Parameter(name = "roomId",description = "조회할 방의 ID 값", in = ParameterIn.PATH ,required = true)
    @ApiResponse(responseCode = "200",description = "쪽지방에서 메시지 조회 완료")
    public ResponseEntity<ApiBaseResponse<List<JjokjiResponseDto>>> showRoomMessage(@PathVariable("roomId") final Long roomId){
        return ResponseEntity.ok(ApiBaseResponse.createSuccess(jjokjiService.ChatListInOneRoom(roomId)));
    }



    private Member findById(final Long memberId){
       return  memberRepository.findById(memberId).orElseThrow(()->new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
    }

}
