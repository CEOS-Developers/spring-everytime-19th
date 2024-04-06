package com.ceos19.everytime.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AddSchoolRequest {
    @NotBlank
    private String name;
}
