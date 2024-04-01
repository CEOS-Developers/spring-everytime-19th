package com.ceos19.springboot.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequestDTO {
    @NotNull
    private Long id;

    private String title;
    private String content;

    private Long likeCount;
    private Long reportCount;
    private Long scrap;

    private Boolean isAnonymous;
    private String filePath;
}
