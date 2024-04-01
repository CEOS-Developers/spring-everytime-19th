package com.ceos19.springboot.dto;

import com.ceos19.springboot.domain.Post;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@AllArgsConstructor
public class PostResponseDTO {
    @NotNull
    private Long id;

    private String title;
    private String content;

    private Long likeCount;
    private Long reportCount;
    private Long scrap;

    private Boolean isAnonymous;
    private String filePath;

    public static PostResponseDTO entityToDto(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getLikeCount(),
                post.getReportCount(),
                post.getScrap(),
                post.isAnonymous(),
                post.getFilePath()
        );
    }
}
