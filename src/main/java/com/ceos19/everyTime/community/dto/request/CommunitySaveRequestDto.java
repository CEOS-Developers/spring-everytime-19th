package com.ceos19.everyTime.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommunitySaveRequestDto {

    @NotEmpty
    @Schema(description = "게시판 이름", example = "자유게시판")
    @Size(min = 4,message = "길이 4이상")
    private String name;

    @Builder
    public CommunitySaveRequestDto(String name){
        this.name=name;
    }

}
