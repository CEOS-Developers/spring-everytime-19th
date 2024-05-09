package com.ceos19.everytime.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinUserRequest {
    @NotNull(message = "아이디가 입력되지 않았습니다")
    private String username;
    @NotNull(message = "비밀번호가 입력되지 않았습니다")
    private String password;
    @NotNull(message = "이름이 입력되지 않았습니다")
    private String name;
    @NotNull(message = "학교가 입력되지 않았습니다")
    private Long schoolId;
    @NotNull(message = "학번가 입력되지 않았습니다")
    private String studentNo;
    @Email(message = "잘못된 이메일 형식입니다")
    private String email;
    @NotNull(message = "Role이 입력되지 않았습니다")
    private String role;
}
