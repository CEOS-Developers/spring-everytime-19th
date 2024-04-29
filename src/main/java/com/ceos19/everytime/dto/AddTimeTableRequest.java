package com.ceos19.everytime.dto;

import com.ceos19.everytime.domain.Semester;
import com.ceos19.everytime.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor
public class AddTimeTableRequest {
    @NotNull
    private String name;
    @NotNull
    private int year;
    @NotNull
    private Semester semester;
}
