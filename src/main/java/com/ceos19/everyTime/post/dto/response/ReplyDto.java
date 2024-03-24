package com.ceos19.everyTime.post.dto.response;

import com.ceos19.everyTime.post.domain.Reply;
import lombok.Getter;

@Getter
public class ReplyDto {
    private String comment;
    private String writer;
    private Long parentId;
    private Long replyId;
    private int likeCount;
    private Long memberId;


    public ReplyDto(Reply reply,String writer){
        this.comment = reply.getContents();
        this.writer = writer;
        this.parentId = reply.getParent()==null?null:reply.getParent().getId();
        this.replyId =reply.getId();
        this.likeCount = reply.getLikeCount();
        this.memberId = reply.getMember().getId();
    }


}
