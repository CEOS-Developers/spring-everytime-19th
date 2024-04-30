package com.ceos19.everyTime.auth.dto;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
public class SignInRequestDto {
    private String userID;
    private String password;
    public UsernamePasswordAuthenticationToken getAuthenticationToken(){
        return new UsernamePasswordAuthenticationToken(userID,password);
    }
}
