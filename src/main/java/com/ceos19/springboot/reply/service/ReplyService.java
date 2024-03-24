package com.ceos19.springboot.reply.service;

import com.ceos19.springboot.comment.dto.CommentRequestDto;
import com.ceos19.springboot.comment.dto.CommentResponseDto;
import com.ceos19.springboot.comment.entity.Comment;
import com.ceos19.springboot.comment.repository.CommentRepository;
import com.ceos19.springboot.common.ErrorType;
import com.ceos19.springboot.common.RestApiException;
import com.ceos19.springboot.post.entity.Post;
import com.ceos19.springboot.post.repository.PostRepository;
import com.ceos19.springboot.reply.dto.ReplyRequestDto;
import com.ceos19.springboot.reply.dto.ReplyResponseDto;
import com.ceos19.springboot.reply.entity.Reply;
import com.ceos19.springboot.reply.repository.ReplyRepository;
import com.ceos19.springboot.user.entity.User;
import com.ceos19.springboot.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReplyService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    public List<ReplyResponseDto> getAllReplies(
            Long commentId
    ) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Comment comment = optionalComment.orElseThrow(
                () -> new RestApiException(ErrorType.NOT_FOUND)
        );

        List<Reply> replies = replyRepository.findAllByComment(comment)g;
        return replies.stream()
                .map(reply -> new ReplyResponseDto(
                        reply.getComment().getCommentId(),
                        reply.getContent(),
                        reply.getLikes()
                ))
                .collect(Collectors.toList());
    }
    // 댓글 작성하기
    public void createReply(UserDetails loginUser, ReplyRequestDto replyRequestDto, Long commentId) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Comment comment = optionalComment.orElseThrow(
                () -> new RestApiException(ErrorType.NOT_FOUND)
        );
        Reply reply = new Reply(replyRequestDto.getContent(), comment,user);
        reply.setLikes(0);
        replyRepository.save(reply);
    }
}
