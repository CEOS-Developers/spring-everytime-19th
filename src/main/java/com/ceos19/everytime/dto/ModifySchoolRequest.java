package com.ceos19.everytime.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ModifySchoolRequest {
    @NotBlank
    private String name;
}
