package com.ceos19.everyTime.post.service;

import com.ceos19.everyTime.community.domain.Community;
import com.ceos19.everyTime.community.repository.CommunityRepository;
import com.ceos19.everyTime.error.ErrorCode;
import com.ceos19.everyTime.error.exception.NotFoundException;
import com.ceos19.everyTime.member.domain.Member;
import com.ceos19.everyTime.post.domain.Post;
import com.ceos19.everyTime.post.domain.PostImage;
import com.ceos19.everyTime.post.domain.Reply;
import com.ceos19.everyTime.post.dto.editor.PostEditor;
import com.ceos19.everyTime.post.dto.request.PostSaveDto;
import com.ceos19.everyTime.post.dto.request.PostEditDto;
import com.ceos19.everyTime.post.dto.response.PostListWithSliceDto;
import com.ceos19.everyTime.post.dto.response.PostResponseDto;
import com.ceos19.everyTime.post.dto.response.PostShortResponseDto;
import com.ceos19.everyTime.post.dto.response.ReplyDto;
import com.ceos19.everyTime.post.repository.PostRepository;
import com.ceos19.everyTime.post.repository.ReplyRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;
    private final PostImageService postImageService;
    private static final String DEFAULT_HIDDEN_NICK_NAME="익명";
    private final ReplyRepository replyRepository;
    private static final String FOR_NULL_NICKNAME="알수없음";
    private static final String POST_IMAGE="post-images";



    //게시물 저장.
    @Transactional
    public Post savePost(PostSaveDto postSaveDto, Long communityId, Member currentMember){
        Community community = communityRepository.findById(communityId).orElseThrow(()->new NotFoundException(
            ErrorCode.MESSAGE_NOT_FOUND));

        //post 내용 저장
        Post post = Post.builder()
            .member(currentMember)
            .community(community)
            .title(postSaveDto.getTitle())
            .contents(postSaveDto.getContents())
            .isQuestion(postSaveDto.isQuestion())
            .isHideNickName(postSaveDto.isHideNickName())
            .build();


        //post 사진 저장.
        postSaveDto.getMultipartFileList().forEach(multipartFile -> {
                String accessUrl = postImageService.saveImage(multipartFile,POST_IMAGE,
                    multipartFile.getOriginalFilename());

                post.saveImage(multipartFile.getOriginalFilename(),accessUrl);
           });

        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(PostEditDto postEditDto,Long postId,Member currentMember){
        Post post=findPost(postId);

        //질문글일 경우 수정 불가
        if(post.isQuestion()==true){
            throw new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND);
        }

        // 현재 member 와 post 작성자가 다른 경우는 수정 불가
        if(post.getMember().getId()!=currentMember.getId()){
            throw new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND);
        }

        /*
        //post에 대해 title,content,질문글 여부, 익명 여부를 수정.
        post.changeTitleAndContentAndIsQuestionAndIsHideNickName(postEditDto.getTitle(), postEditDto.getContents(),
             postEditDto.isQuestion(),postEditDto.isHideNickName());
        */


        //Editor 를 이용해서 수정해보기.
        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();


        PostEditor postEditor = editorBuilder.title(postEditDto.getTitle())
                .isQuestion(postEditDto.isQuestion())
                    .hideNickName(postEditDto.isHideNickName())
                        .content(postEditDto.getContents())
                            .build();

        post.edit(postEditor);

        //각각의 이미지에 대해 링크에 대한 S3 원본 사진 삭제
            post.getPostImageList().forEach(postImage -> {
                  postImageService.deleteImage(postImage.getAccessUrl());
            });

            //연관관계를 끊어줌으로 DB 삭제
            post.getPostImageList().clear();



            //새로운 이미지에 대해 다시 저장.
            postEditDto.getMultipartFileList().forEach(multipartFile -> {
                String accessUrl = postImageService.saveImage(multipartFile,POST_IMAGE,
                    multipartFile.getOriginalFilename());

                post.saveImage(multipartFile.getOriginalFilename(),accessUrl);
            });


            return post;
    }

    @Transactional
    public void deletePost(Long postId,Member currentMember){

        Post post = findPost(postId);

        //현재 삭제하려는 member 와 post 의 작성자 ID가 아닌 경우 예외
        if(post.getMember().getId()!=currentMember.getId()){
            throw new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND);
        }

        //post 이미지 s3 삭제
        post.getPostImageList().forEach(postImage -> {
            postImageService.deleteImage(postImage.getAccessUrl());
        });


        postRepository.delete(post);
    }


    // 무한 스크롤 이용, Post List 확인.
    public PostListWithSliceDto showPostList(Long communityId, Pageable pageable){
        Slice<PostShortResponseDto> postShortResponseDtos = postRepository.
            findByCommunityIdOrderByCreatedAt(communityId,pageable)
            .map(p->{
                return new PostShortResponseDto(p,makeNickNameByPost(p));
            });


        return new PostListWithSliceDto(postShortResponseDtos.getContent(),postShortResponseDtos.hasNext());

    }


    //Post 에 대해 단건 조회 확인.
    public PostResponseDto showDetailsPost(Long postId){
        //Post + PostImage + Post 게시글 작성자 함께 영속화
        Post post = findPostWithFetchMemberAndImage(postId);

        //첫 댓글 Reply (대댓글 X) + 부모댓글 작성자 함께 영속화
        List<Reply> parentReply = replyRepository.findParentReplyByPostIdWithFetchMember(postId);

        List<ReplyDto> replyDtoList = new ArrayList<>();

        //부모-> 자식 순으로 DTO 순서 저장.
        for(Reply parent: parentReply){
            replyDtoList.add(new ReplyDto(parent,makeNickNameByReply(parent)));


            parent.getChildList().stream().forEach(r->{
                replyDtoList.add(new ReplyDto(r,makeNickNameByReply(r)));
            });

/*
            List<Reply> childList = replyRepository.findChildByParentId(parent.getId());
            childList.stream().forEach(r->{
                replyDtoList.add(new ReplyDto(r,makeNickNameByReply(r)));
            });
*/
        }

        //이미지 accessUrl.
        List<String> accessUrlList = post.getPostImageList().stream().map(postImage -> {
            return postImage.getAccessUrl();
        }).collect(Collectors.toList());

        return new PostResponseDto(post,makeNickNameByPost(post),accessUrlList,replyDtoList);
    }


    private Post findPost(Long postId){
        return postRepository.findById(postId).orElseThrow(()->new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
    }

    private Post findPostWithFetchMemberAndImage(Long postId){
        return postRepository.findPostByPostIdWithFetchMemberAndPostImageList(postId).orElseThrow(()->new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
    }

    // Post 작성자 반환
    private String makeNickNameByPost(Post post){
        // Member 가 회원가입을 탈퇴할 경우 Post가 사라지지 않고 (알수없음) 으로 바뀌는듯? 그걸 위한 체크
        if(post.getMember()==null){
            return FOR_NULL_NICKNAME;
        }
        return post.isHideNickName()?DEFAULT_HIDDEN_NICK_NAME:post.getMember().getNickName();
    }

    // Reply 의 작성자 반환
    private String makeNickNameByReply(Reply reply){
        // 부모 댓글에 답글이 달렸을 경우 부모 댓글이 삭제되도 (알수없음) 으로 바뀌는 듯? 그걸 위한 체크
        if(reply.getMember()==null){
            return FOR_NULL_NICKNAME;
        }
        return reply.getWriter();
    }


    //익명 1,2 를 생성 위한 메서드
    public String makeNextNickNameForHideNickName(Post post){
        return DEFAULT_HIDDEN_NICK_NAME+post.getIncreaseHideNameSequence();
    }





}
