package com.ceos19.everyTime.jjokji.domain;

import com.ceos19.everyTime.common.BaseEntity;
import com.ceos19.everyTime.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Jjokji extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jjokji_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jjokji_room_id",nullable = false,updatable = false)
    private JjokjiRoom jjokjiRoom;


    @Column(nullable = false,updatable = false)
    private String message;

    @Builder
    public Jjokji(Member member,JjokjiRoom jjokjiRoom,String message){
        this.member=member;
        this.jjokjiRoom=jjokjiRoom;
        this.message=message;
    }





}
