package com.ceos19.everytime.domain.AboutMessage;


import com.ceos19.everytime.domain.AboutUser.User;
import com.ceos19.everytime.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class MessageBox extends BaseTimeEntity {

    @Id
    @Column(name="messagebox_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long messageboxId;

    @Column(name="message_num", nullable = false)
    private Long messageNum=0L;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="partner_user_id")
    private User user;

}
