package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseTimeEntity{

    public static final int MAX_CONTENT_LENGTH = 2000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;


    @Builder
    public Message (Member sender, Member receiver, String content){
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.readStatus = ReadStatus.NOT_READ;
    }

    private boolean validateContent(String content){
        if(content.isEmpty() || content.length()> MAX_CONTENT_LENGTH)
            return false;
        return true;
    }

    public void updateReadStatus(){
        this.readStatus = ReadStatus.READ;
    }

}
