package com.ceos19.everytime.service;

import com.ceos19.everytime.domain.*;
import com.ceos19.everytime.dto.AddAttachmentRequest;
import com.ceos19.everytime.dto.AddPostRequest;
import com.ceos19.everytime.dto.AttachmentDto;
import com.ceos19.everytime.dto.ModifyPostRequest;
import com.ceos19.everytime.exception.AppException;
import com.ceos19.everytime.exception.ErrorCode;
import com.ceos19.everytime.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.ceos19.everytime.exception.ErrorCode.*;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final BoardRepository boardRepository;

    public Long addPost(Post post, Attachment... attachments) {
        postRepository.save(post);
        for (Attachment attachment : attachments) {
            if (attachment.getId() != null) {
                log.error("에러 내용: 게시물 등록 실패 " +
                        "발생 원인: 이미 다른 곳에 등록된 파일을 해당 게시물에 재 등록함");
                throw new AppException(DATA_ALREADY_EXISTED, "이미 등록된 파일입니다");
            }
            post.addAttachment(attachment);
        }
        return post.getId();
    }

    public Post addPost(AddPostRequest request, Long boardId) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> {
            log.error("에러 내용: 게시물 등록 실패 " +
                    "발생 원인: 존재하지 않는 User의 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 유저입니다");
        });
        Board board = boardRepository.findById(boardId).orElseThrow(() -> {
            log.error("에러 내용: 게시물 등록 실패 " +
                    "발생 원인: 존재하지 않는 Board PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 게시판입니다");
        });

        // Post 생성
        Post post = Post.builder().
                title(request.getTitle())
                .content(request.getContent())
                .isQuestion(request.isQuestion())
                .isAnonymous(request.isAnonymous())
                .board(board)
                .author(user)
                .build();

        // Post에 첨부파일 추가
        for (AttachmentDto dto : request.getAttachments()) {
            Attachment attachment
                    = new Attachment(dto.getOriginalFileName(), dto.getStoredPath(), dto.getAttachmentType());

            post.addAttachment(attachment);
        }
        postRepository.save(post);
        return post;
    }

    @Transactional(readOnly = true)
    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> {
                    log.error("에러 내용: 게시물 조회 실패 " +
                            "발생 원인: 존재하지 않는 PK 값으로 조회");
                    return new AppException(NO_DATA_EXISTED, "존재하지 않는 게시물입니다");
                });
    }

    @Transactional(readOnly = true)
    public List<Post> findPostByName(Long boardId, String name) {
        boardRepository.findById(boardId)
                .orElseThrow(() -> {
                            log.error("에러 내용: 게시판 조회 실패 " +
                                    "발생 원인: 존재하지 않는 PK 값으로 조회");
                            return new AppException(NO_DATA_EXISTED, "존재하지 않는 게시판입니다");
                        }
                );
        return postRepository.findByBoardIdAndTitle(boardId, name);
    }

    @Transactional(readOnly = true)
    public List<Post> findPostByAuthorId(Long authorId) {
        return postRepository.findByAuthorId(authorId);
    }

    @Transactional(readOnly = true)
    public List<Post> findPostByBoardId(Long boardId) {
        boardRepository.findById(boardId)
                .orElseThrow(() -> {
                            log.error("에러 내용: 게시판 조회 실패 " +
                                    "발생 원인: 존재하지 않는 PK 값으로 조회");
                            return new AppException(NO_DATA_EXISTED, "존재하지 않는 게시판입니다");
                        }
                );
        return postRepository.findByBoardId(boardId);
    }

    @Transactional(readOnly = true)
    public List<Post> findPostByBoardIdAndTitle(Long boardId, String title) {
        boardRepository.findById(boardId)
                .orElseThrow(() -> {
                            log.error("에러 내용: 게시판 조회 실패 " +
                                    "발생 원인: 존재하지 않는 PK 값으로 조회");
                            return new AppException(NO_DATA_EXISTED, "존재하지 않는 게시판입니다");
                        }
                );
        return postRepository.findByBoardIdAndTitle(boardId, title);
    }

    @Transactional(readOnly = true)
    public List<Post> findPostByBoardIdAndCreatedDate(Long boardId, LocalDate createdDate) {
        boardRepository.findById(boardId)
                .orElseThrow(() -> {
                            log.error("에러 내용: 게시판 조회 실패 " +
                                    "발생 원인: 존재하지 않는 PK 값으로 조회");
                            return new AppException(NO_DATA_EXISTED, "존재하지 않는 게시판입니다");
                        }
                );
        return postRepository.findByBoardIdAndCreatedDate(boardId, createdDate.getYear(), createdDate.getMonthValue(), createdDate.getDayOfMonth());
    }

    public void removePost(Long postId) {
        postRepository.findById(postId).orElseThrow(() -> {
            log.error("에러 내용: 게시물 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 게시물입니다");
        });

        // 연관관계 제거
        List<Comment> comments = commentRepository.findByPostId(postId);
        for (Comment comment : comments) {
            comment.removeParentComment();
        }

        // 연관관계 제거
        commentRepository.deleteAllByPostId(postId);

        // 연관관계 제거
        postLikeRepository.deleteAllByPostId(postId);

        postRepository.deleteById(postId);
    }

    public void modifyPost(Long postId, ModifyPostRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            log.error("에러 내용: 게시물 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 게시물입니다");
        });

        post.updateContent(request.getContent());
        post.updateIsQuestion(request.getIsQuestion());
        post.updateIsAnonymous(request.getIsAnonymous());
    }

    public Long addAttachment(Long postId, AddAttachmentRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            log.error("에러 내용: 게시물 조회 실패 " +
                    "발생 원인: 존재하지 않는 PK 값으로 조회");
            return new AppException(NO_DATA_EXISTED, "존재하지 않는 게시물입니다");
        });
        Attachment attachment =
                new Attachment(request.getOriginalFileName(), request.getStoredPath(), request.getAttachmentType());
        post.addAttachment(attachment);

        return attachment.getId();
    }
}
