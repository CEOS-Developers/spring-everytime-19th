package com.ceos19.everyTime.jjokji.domain;

import com.ceos19.everyTime.common.BaseEntity;
import com.ceos19.everyTime.member.domain.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class JjokjiRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jjokji_room_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_sender_id",updatable = false)
    private Member firstSender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_receiver_id",updatable = false)
    private Member firstReceiver;

    @Builder
    public JjokjiRoom(Member firstSender,Member firstReceiver){
        this.firstSender=firstSender;
        this.firstReceiver= firstReceiver;
    }








}
