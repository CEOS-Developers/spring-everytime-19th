package com.ceos19.everyTime.post.controller;

import com.ceos19.everyTime.error.ErrorCode;
import com.ceos19.everyTime.error.exception.NotFoundException;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.member.repository.MemberRepository;
import com.ceos19.everyTime.post.dto.request.ReplyCommentSaveRequestDto;
import com.ceos19.everyTime.post.service.LikeReplyService;
import com.ceos19.everyTime.post.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
@Tag(name = "게시글에 대한 답글 컨트롤러", description = "게시글 답글 관련 API")
public class ReplyController {
    private final ReplyService replyService;
    private final MemberRepository memberRepository;
    private final LikeReplyService likeReplyService;

    @PostMapping("/{postId}/{memberId}")
    @Operation(summary = "답글 저장", description = "게시글에 답글을 작성합니다")
    @Parameters({
        @Parameter(name = "postId",description = "답글을 저장할 게시물의 ID값", in = ParameterIn.PATH ,required = true),
        @Parameter(name = "memberId",description = "답글을 저장할 사람의 ID 값", in = ParameterIn.PATH ,required = true)
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",description = "답글 저장 완료"),
        @ApiResponse(responseCode = "404",description = "자원 식별 불가")
    })
    public ResponseEntity<Void> saveReply(@PathVariable("postId") Long postId,@PathVariable("memberId") Long memberId,@RequestBody ReplyCommentSaveRequestDto replyCommentSaveRequestDto){
        replyService.saveComment(postId, replyCommentSaveRequestDto,getFindMember(memberId));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{replyId}/{memberId}")
    @Operation(summary = "게시글 삭제", description = "게시글에 답글을 삭제합니다.")
    @Parameters({
        @Parameter(name = "replyId",description = "삭제할 답글 ID 값", in = ParameterIn.PATH,required = true),
        @Parameter(name = "memberId",description = "답글을 삭제할 사람의 ID값" , in = ParameterIn.PATH, required = true)
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "답글 삭제 완료"),
        @ApiResponse(responseCode = "404",description = "자원 식별 불가")
    })
    public void deleteReply(@PathVariable("replyId") Long replyId,@PathVariable("memberId") Long memberId){
        replyService.deleteComment(replyId,getFindMember(memberId));
    }

    @PostMapping("/like/{replyId}/{memberId}")
    @Operation(summary = "답글 좋아요", description = "게시글에 대한 답글에 좋아요 를 누르거나 취소합니다. ")
    @Parameters({
        @Parameter(name = "replyId",description = "좋아할 답글 ID 값", in = ParameterIn.PATH,required = true),
        @Parameter(name = "memberId",description = "좋아요할 사람의 ID값 " , in = ParameterIn.PATH, required = true)
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "답글 좋아요/싫어요 완료"),
        @ApiResponse(responseCode = "404",description = "자원 식별 불가")
    })
    public void likeReply(@PathVariable("replyId") Long replyId,@PathVariable("memberId") Long memberId){
        likeReplyService.likeReply(getFindMember(memberId),replyId);
    }







    private Member getMember(){
        return memberRepository.findById(1L).orElseThrow(()->new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
    }

    private Member getMember2(){
        return memberRepository.findById(2L).orElseThrow(()->new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
    }

    private Member getFindMember(Long id){
        return memberRepository.findById(id).orElseThrow(()->new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
    }

}
