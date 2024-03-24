package com.ceos19.everyTime.post.dto.response;

import com.ceos19.everyTime.post.domain.Post;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
    private List<ReplyDto> replyDtoList = new ArrayList<>();




    public PostResponseDto(Post post,String writer,List<String> accessUrlList,List<ReplyDto> replyDtoList){
        this.postId = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.replyCount = post.getReplyCount();
        this.likeCount = post.getLikeCount();
        this.writer = writer;
        this.createdAt = post.getCreatedAt();
        this.replyDtoList = replyDtoList;
        this.accessUrlList = accessUrlList;
        this.memberId = post.getMember().getId();
    }
}
