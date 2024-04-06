package com.ceos19.springeverytime.user.dto.response;

import com.ceos19.springeverytime.user.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseUserDto {
    private Long id;
    private String LoginId;
    private String name;

    public static ResponseUserDto of (User user){
        return ResponseUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .LoginId(user.getLoginId())
                .build();
    }
}
