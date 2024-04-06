package com.ceos19.everyTime.post.dto.response;

import com.ceos19.everyTime.post.domain.Post;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostResponseDto {
    private Long postId;
    private String title;
    private String contents;
    private int replyCount;
    private int likeCount;
    private String writer;
    private Long memberId;
    private LocalDateTime createdAt;
    private List<String> accessUrlList = new ArrayList<>();
    private List<ReplyResponseDto> replyResponseDtoList = new ArrayList<>();

    public static PostResponseDto of(Post post,String writer,List<String> accessUrlList,List<ReplyResponseDto> replyResponseDtoList){
        return new PostResponseDto(post.getId(),post.getTitle(),post.getContents(),post.getReplyCount(),post.getLikeCount(),
            writer,post.getMember().getId(),post.getCreatedAt(),accessUrlList,replyResponseDtoList);
    }
}
