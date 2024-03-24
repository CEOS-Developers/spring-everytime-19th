package com.ceos19.everytime.domain.AboutUser;


import com.ceos19.everytime.domain.AboutUser.User;
import com.ceos19.everytime.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Friend extends BaseTimeEntity {
    @Id
    @Column(name="friend_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long friendId;

    @Column(name="is_accepted", nullable = false)
    @Builder.Default
    private boolean isAccepted=false;

    @ManyToOne
    @JoinColumn(name="request_user_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name="response_user_id")
    private User user2;

}
