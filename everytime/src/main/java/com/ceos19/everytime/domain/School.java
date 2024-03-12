package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class School {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "school_id")
    private Long id;

    @NotBlank(message = "학교명은 비어 있을 수 없습니다.")
    private String name;
    @NotBlank(message = "학과는 비어 있을 수 없습니다.")
    private String department;
}
