package com.ceos19.everyTime.member.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberSignUpDto {


    private String name;
    private String loginId;
    private String password;

    private String nickName;

    @Builder
    public MemberSignUpDto(String name,String loginId,String password,String nickName){
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.nickName = nickName;
    }

}
