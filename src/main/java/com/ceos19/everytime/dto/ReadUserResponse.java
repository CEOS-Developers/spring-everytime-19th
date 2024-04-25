package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadUserResponse {
    private Long id;
    private String name;
    private String studentNo;
    private String email;

    public static ReadUserResponse from(User user) {
        return new ReadUserResponse(user.getId(), user.getName(), user.getStudentNo(), user.getEmail());
    }
}
