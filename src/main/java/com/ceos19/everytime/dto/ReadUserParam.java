package com.ceos19.everytime.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadUserParam {
    @Email
    private String email;
    private Long school_id;
    private String studentNo;
}
