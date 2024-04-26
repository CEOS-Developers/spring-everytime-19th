package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.Role;
import com.ceos19.everytime.domain.School;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor
public class AddUserRequest {
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
    private Role role;
}
