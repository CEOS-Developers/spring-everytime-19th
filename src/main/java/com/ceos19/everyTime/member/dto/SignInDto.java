package com.ceos19.everyTime.member.dto;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
public class SignInDto {
    private String userID;
    private String password;
    public UsernamePasswordAuthenticationToken getAuthenticationToken(){
        return new UsernamePasswordAuthenticationToken(userID,password);
    }
}
