package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
@ToString
public class ChattingRoom {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "chatting_room_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "participant1_id")
    private User participant1;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "participant2_id")
    private User participant2;

    public ChattingRoom(User participant1, User participant2) {
        this.participant1 = participant1;
        this.participant2 = participant2;
    }
}
