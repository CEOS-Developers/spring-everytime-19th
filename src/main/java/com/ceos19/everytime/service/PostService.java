package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.Board;
import com.ceos19.everytime.domain.Member;
import com.ceos19.everytime.domain.Post;
import com.ceos19.everytime.dto.*;
import com.ceos19.everytime.exception.CustomException;
import com.ceos19.everytime.repository.BoardRepository;
import com.ceos19.everytime.repository.MemberRepository;
import com.ceos19.everytime.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ceos19.everytime.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

    public static final int MAX_TITLE_LENGTH = 100;
    public static final int MAX_CONTENT_LENGTH = 2000;

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public Long publish (CreatePostRequest createPostRequest){
        final Member author = memberRepository.findById(createPostRequest.getAuthorId())
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));
        final Board board = boardRepository.findById(createPostRequest.getBoardId())
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));

        Post post = new Post(createPostRequest.getTitle(), createPostRequest.getContent(), author, board, createPostRequest.isAnonymous());

        return postRepository.save(post)
                .getId();
    }

    @Transactional(readOnly = true)
    public PostResponse findPost (Long postId){
        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getAuthor(), post.getBoard(), post.isAnonymous(), post.getLikes());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findEveryPost (Long boardId){
        final Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));

        List<PostResponse> postResponseList = new ArrayList<>();
        for(Post post : postRepository.findAllByBoardId(boardId)){
            postResponseList.add(new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getAuthor(), post.getBoard(), post.isAnonymous(), post.getLikes()));
        }

        return postResponseList;
    }

    public void updatePost(Long postId, PostUpdateRequest postUpdateRequest){
        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));

        post.changeTitle(postUpdateRequest.getTitle());
        post.changeContent(postUpdateRequest.getContent());
        post.changeAnonymous(postUpdateRequest.isAnonymous());
    }

    public void delete(Long postId, DeleteRequest deleteRequest){
        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));
        final Member member = memberRepository.findById(deleteRequest.getMemberId())
                .orElseThrow(() -> new CustomException(DATA_NOT_FOUND));

        if(post.getAuthor().equals(member))
            postRepository.delete(post);
        else
            throw new CustomException(INVALID_PARAMETER);

    }

}
