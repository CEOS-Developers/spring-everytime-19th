package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Getter
public class ChatRoom {
    @Id
    @GeneratedValue
    private Long roomId;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user1_id")
    private User member1;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user2_id")
    private User member2;
}
