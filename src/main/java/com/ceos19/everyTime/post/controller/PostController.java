package com.ceos19.everyTime.post.controller;

import com.ceos19.everyTime.common.ApiBaseResponse;
import com.ceos19.everyTime.error.ErrorCode;
import com.ceos19.everyTime.error.exception.NotFoundException;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.member.repository.MemberRepository;
import com.ceos19.everyTime.post.dto.request.PostEditRequestDto;
import com.ceos19.everyTime.post.dto.request.PostSaveRequestDto;
import com.ceos19.everyTime.post.dto.response.PostListWithSliceResponseDto;
import com.ceos19.everyTime.post.dto.response.PostResponseDto;
import com.ceos19.everyTime.post.service.LikePostService;
import com.ceos19.everyTime.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@Tag(name = "게시글에 대한 컨트롤러", description = "게시글 관련 API")
public class PostController {
    private final PostService postService;
    private final MemberRepository memberRepository;
    private final LikePostService likePostService;

    @PostMapping(value = "/{communityId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 저장", description = "게시판에 게시글을 작성합니다.")
    @Parameters({
        @Parameter(name = "communityId",description = "커뮤니티 즉 게시판의 ID 를 입력해주세요", in = ParameterIn.PATH ,required = true),
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",description = "게시물 생성 완료"),
        @ApiResponse(responseCode = "404",description = "자원 식별 불가")
    })
    public ResponseEntity<Void> savePost(@PathVariable("communityId") final Long communityId,@ModelAttribute @Valid final PostSaveRequestDto postSaveRequestDto){
        postService.savePost(postSaveRequestDto,communityId,getMember());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping(value = "/{postId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 수정", description = "게시판에 게시글을 수정합니다.")
    @Parameters({
        @Parameter(name = "postId",description = "수정할 게시글의 ID를 입력해주세요", in = ParameterIn.PATH ,required = true)
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "업데이트 성공"),
        @ApiResponse(responseCode = "404",description = "자원 식별 불가")
    })
    public ResponseEntity<Void> changePost(@PathVariable("postId") final Long postId, @ModelAttribute @Valid final PostEditRequestDto postEditRequestDto){
        postService.updatePost(postEditRequestDto,postId,getMember());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제", description = "게시판에 게시글을 삭제합니다.")
    @Parameters({
        @Parameter(name = "postId",description = "삭제할 게시글의 id를 입력해주세요", in = ParameterIn.PATH ,required = true)
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "삭제 성공"),
        @ApiResponse(responseCode = "404",description = "자원 식별 불가")
    })
    public void deletePost(@PathVariable("postId") final Long postId){
        postService.deletePost(postId,getMember());
    }

    @GetMapping("/{communityId}/list")
    @Operation(summary = "게시글 리스트 조회", description = "게시판에 게시글 리스트를 보여줍니다. ")
    @Parameters({
        @Parameter(name = "communityId",description = "조회할 게시판을 선택해주세요", in = ParameterIn.PATH ,required = true),
        /*@Parameter(name = "page",description = "페이지번호, 0부터 시작",in = ParameterIn.QUERY),
        @Parameter(name = "size 등등등",description = "페이지 당 아이템 갯수",in = ParameterIn.QUERY)*/
    })
    @ApiResponse(responseCode = "200",description = "게시글 리스트 조회 성공")
    public ResponseEntity<ApiBaseResponse<PostListWithSliceResponseDto>> showPostList(@PathVariable("communityId") final Long communityId,
        @PageableDefault(size = 10,
            sort = "createdAt",
            direction = Sort.Direction.DESC,
            page = 0
        )Pageable pageable){
        return ResponseEntity.ok(ApiBaseResponse.createSuccess(postService.showPostList(communityId,pageable)));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 단건 조회", description = "게시판에 게시글을 단건 조회합니다. ")
    @Parameters({
        @Parameter(name = "postId",description = "단건 조회할 게시글의 id를 입력해주세요", in = ParameterIn.PATH ,required = true)
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "게시글 리스트 조회 성공"),
        @ApiResponse(responseCode = "404",description = "자원 식별 불가")
    })
    public ResponseEntity<ApiBaseResponse<PostResponseDto>> showPost(@PathVariable("postId") final Long postId){
        return ResponseEntity.ok(ApiBaseResponse.createSuccess(postService.showDetailsPost(postId)));
    }


    @PostMapping("/like/{postId}/{memberId}")
    @Operation(summary = "게시글 좋아요", description = "게시글에 좋아요 혹은 좋아요 취소를 누릅니다. ")
    @Parameters({
        @Parameter(name = "postId",description = "좋아요/취소 를 누를 게시글의 id를 입력해주세요", in = ParameterIn.PATH ,required = true)
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "게시글 좋아요 성공"),
        @ApiResponse(responseCode = "404",description = "자원 식별 불가")
    })
    public void likePost(@PathVariable("postId") final Long postId){
        likePostService.likePost(getMember(),postId);
    }




    private Member getfindMember(Long id){
        return memberRepository.findById(id).get();
    }

    private Member getMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return memberRepository.findMemberByLoginId(authentication.getName()).orElseThrow(()->new NotFoundException(
                ErrorCode.MESSAGE_NOT_FOUND));
    }
}
