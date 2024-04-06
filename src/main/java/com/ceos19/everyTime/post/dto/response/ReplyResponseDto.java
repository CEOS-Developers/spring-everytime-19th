package com.ceos19.everyTime.post.dto.response;

import com.ceos19.everyTime.post.domain.Reply;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReplyResponseDto {
    private String comment;
    private String writer;
    private Long parentId;
    private Long replyId;
    private int likeCount;
    private Long memberId;

    public static ReplyResponseDto of(Reply reply,String writer,String contents){

        return new ReplyResponseDto(contents,writer,reply.getParent()==null?null:reply.getParent().getId(),
            reply.getId(), reply.getLikeCount(),reply.getMember().getId());
    }




}
