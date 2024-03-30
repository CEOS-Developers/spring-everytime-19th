package com.ceos19.everytime.domain.AboutMessage;


import com.ceos19.everytime.domain.AboutUser.User;
import com.ceos19.everytime.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MessageBox extends BaseTimeEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageboxId;

    @Column(nullable = false)
    private Long messageNum = 0L;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_user_id")
    private User user;

    @Builder
    public MessageBox(Long messageboxId, Long messageNum, User user) {
        this.messageboxId = messageboxId;
        this.messageNum = messageNum;
        this.user = user;
    }
}
