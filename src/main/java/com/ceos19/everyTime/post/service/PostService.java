package com.ceos19.everyTime.post.service;

import com.ceos19.everyTime.community.domain.Community;
import com.ceos19.everyTime.community.repository.CommunityRepository;
import com.ceos19.everyTime.error.ErrorCode;
import com.ceos19.everyTime.error.exception.NotFoundException;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.post.domain.Post;
import com.ceos19.everyTime.post.domain.Reply;
import com.ceos19.everyTime.post.dto.editor.PostEditor;
import com.ceos19.everyTime.post.dto.request.PostSaveRequestDto;
import com.ceos19.everyTime.post.dto.request.PostEditRequestDto;
import com.ceos19.everyTime.post.dto.response.PostListWithSliceResponseDto;
import com.ceos19.everyTime.post.dto.response.PostResponseDto;
import com.ceos19.everyTime.post.dto.response.PostShortResponseDto;
import com.ceos19.everyTime.post.dto.response.ReplyResponseDto;
import com.ceos19.everyTime.post.repository.PostRepository;
import com.ceos19.everyTime.post.repository.ReplyRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;
    private final PostImageService postImageService;
    private static final String DEFAULT_HIDDEN_NICK_NAME="익명";
    private final ReplyRepository replyRepository;
    private static final String FOR_DELETED_NICKNAME="알수없음";
    private static final String FOR_DELETED_CONTENTS="메시지가 삭제되었습니다";
    private static final String POST_IMAGE="post-images";



    //게시물 저장.
    @Transactional
    public Post savePost(final PostSaveRequestDto postSaveRequestDto, final Long communityId, final Member currentMember){

        final Community community = communityRepository.findById(communityId).orElseThrow(()->new NotFoundException(
            ErrorCode.MESSAGE_NOT_FOUND));

        //post 내용 저장
        final Post post = Post.builder()
            .member(currentMember)
            .community(community)
            .title(postSaveRequestDto.getTitle())
            .contents(postSaveRequestDto.getContents())
            .isQuestion(postSaveRequestDto.isQuestion())
            .isHideNickName(postSaveRequestDto.isHideNickName())
            .build();

        //post 사진 저장.
        saveImages(post,postSaveRequestDto.getMultipartFileList());

        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(final PostEditRequestDto postEditRequestDto,final Long postId,final Member currentMember){

        final Post post=findPost(postId);

        //업데이트가 가능한지 체크
        validateEditable(post,currentMember);

        /*
        //post에 대해 title,content,질문글 여부, 익명 여부를 수정.
        post.changeTitleAndContentAndIsQuestionAndIsHideNickName(postEditDto.getTitle(), postEditDto.getContents(),
             postEditDto.isQuestion(),postEditDto.isHideNickName());
        */


        //Editor 를 이용해서 수정해보기.
        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();


        PostEditor postEditor = editorBuilder.title(postEditRequestDto.getTitle())
                .isQuestion(postEditRequestDto.isQuestion())
                    .hideNickName(postEditRequestDto.isHideNickName())
                        .content(postEditRequestDto.getContents())
                            .build();

        post.edit(postEditor);

        //각각의 이미지에 대해 링크에 대한 S3 원본 사진 삭제
        deleteImages(post);
        saveImages(post,postEditRequestDto.getMultipartFileList());


        return post;
    }

    @Transactional
    public void deletePost(final Long postId,final Member currentMember){

        final Post post = findPost(postId);

        //현재 삭제하려는 member 와 post 의 작성자 ID가 아닌 경우 예외
        validateEditable(post,currentMember);

        //post 이미지 s3 삭제
        deleteImages(post);

        postRepository.delete(post);
    }


    // 무한 스크롤 이용, Post List 확인.
    public PostListWithSliceResponseDto showPostList(final Long communityId, Pageable pageable){

        Slice<PostShortResponseDto> postShortResponseDtos = postRepository.
            findByCommunityIdOrderByCreatedAt(communityId,pageable)
            .map(p->PostShortResponseDto.of(p,makeNickNameForPost(p)));


        return PostListWithSliceResponseDto.of(postShortResponseDtos.getContent(),postShortResponseDtos.hasNext());

    }


    //Post 에 대해 단건 조회 확인.
    public PostResponseDto showDetailsPost(final Long postId){
        //Post + PostImage + Post 게시글 작성자 함께 영속화
        Post post = findPostWithFetchMemberAndImage(postId);

        //첫 댓글 Reply (대댓글 X) + 부모댓글 작성자 함께 영속화
        List<Reply> parentReply = replyRepository.findParentReplyByPostIdWithFetchMember(postId);

        List<ReplyResponseDto> replyResponseDtoList = new ArrayList<>();

        //부모-> 자식 순으로 DTO 순서 저장.
        for(Reply parent: parentReply){

            replyResponseDtoList.add(ReplyResponseDto.of(parent,makeNickNameForReply(parent),makeContentsForReply(parent)));

  /*          parent.getChildList().forEach(r->{
                if(!r.isDeleted()){
                    replyResponseDtoList.add(ReplyResponseDto.of(r,r.getWriter(),r.getContents()));
                }
            });
*/
            List<Reply> childList = replyRepository.findChildByParentIdOrderByCreatedAt(parent.getId());
            childList.stream().forEach(r->{
                replyResponseDtoList.add(ReplyResponseDto.of(r,r.getWriter(),r.getContents()));
            });
        }

        //이미지 accessUrl.
        List<String> accessUrlList = post.getPostImageList().stream().map(postImage -> {
            return postImage.getAccessUrl();
        }).collect(Collectors.toList());

        return PostResponseDto.of(post,makeNickNameForPost(post),accessUrlList,
            replyResponseDtoList);
    }


    private Post findPost(final Long postId){
        return postRepository.findById(postId).orElseThrow(()->new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
    }

    private Post findPostWithFetchMemberAndImage(final Long postId){
        return postRepository.findPostByPostIdWithFetchMemberAndPostImageList(postId).orElseThrow(()->new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
    }

    // Post 작성자 반환
    private String makeNickNameForPost(final Post post){
        // Member 가 회원가입을 탈퇴할 경우 Post가 사라지지 않고 (알수없음) 으로 바뀌는듯? 그걸 위한 체크
        if(post.getMember()==null){
            return FOR_DELETED_NICKNAME;
        }
        return post.isHideNickName()?DEFAULT_HIDDEN_NICK_NAME:post.getMember().getNickName();
    }

    // Reply 의 작성자 반환
    /*private String makeNickNameByReply(Reply reply){
        // 부모 댓글에 답글이 달렸을 경우 부모 댓글이 삭제되도 (알수없음) 으로 바뀌는 듯? 그걸 위한 체크
        if(reply.getMember()==null){
            return FOR_NULL_NICKNAME;
        }
        return reply.getWriter();
    }*/

    private String makeNickNameForReply(final Reply reply){
        if(reply.isDeleted()){
           return FOR_DELETED_NICKNAME;
        }

        return reply.getWriter();
    }

    private String makeContentsForReply(final Reply reply){
        if(reply.isDeleted()){
            return FOR_DELETED_CONTENTS;
        }

        return reply.getContents();
    }


    //익명 1,2 를 생성 위한 메서드
    public String makeNextNickNameForHideNickName(final Post post){
        return DEFAULT_HIDDEN_NICK_NAME+post.getIncreaseHideNameSequence();
    }


    private void validateEditable(final Post post,final Member member){
        if(post.isQuestion()){
            throw new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND);
        }

        if(post.getMember().getId()!=member.getId()){
            throw new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND);
        }
    }


    //s3 저장 + 연관관계 저장
    private void saveImages(final Post post,final List<MultipartFile> multipartFileList){
        multipartFileList.forEach(multipartFile -> {
            String accessUrl = postImageService.saveImage(multipartFile,POST_IMAGE,
                multipartFile.getOriginalFilename());

            post.saveImage(multipartFile.getOriginalFilename(),accessUrl);
        });
    }

    // s3 삭제 + 연관관계 삭제
    private void deleteImages(final Post post){
        post.getPostImageList().forEach(postImage -> {
            postImageService.deleteImage(postImage.getAccessUrl());
        });

        //연관관계를 끊어줌으로 DB 삭제
        post.getPostImageList().clear();
    }




}
