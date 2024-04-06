package com.ceos19.everytime.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifySchoolRequest {
    @NotBlank
    private String name;
}
