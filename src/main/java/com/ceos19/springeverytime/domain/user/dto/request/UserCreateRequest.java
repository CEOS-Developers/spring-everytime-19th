package com.ceos19.springeverytime.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserCreateRequest {
    @NotNull private final String loginId;
    @NotNull private final String password;
    @NotNull private final String name;
    @NotNull private final String nickname;
    @NotNull private final String major;
    @NotNull private final String admissionYear;
    @NotNull private final String email;
}
