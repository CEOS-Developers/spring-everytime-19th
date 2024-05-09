package com.ceos19.springboot.dto;

import com.ceos19.springboot.domain.Authority;
import com.ceos19.springboot.domain.School;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @NotNull
    @Size(min = 3, max = 50)
    private String nickname;

    @NotNull
    private Boolean isAdmin;

    @NotNull
    private String userLast;

    @NotNull
    private String userFirst;

    @NotNull
    private String Email;

    @NotNull
    private Boolean isBanned;

    @NotNull
    private School school;

    @NotNull
    private Boolean activated;

    @NotNull
    private Set<Authority> authorities;

}