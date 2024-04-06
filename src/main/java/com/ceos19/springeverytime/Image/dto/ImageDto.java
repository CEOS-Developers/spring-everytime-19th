package com.ceos19.springeverytime.Image.dto;

import com.ceos19.springeverytime.Image.domain.Image;
import com.ceos19.springeverytime.post.domain.Post;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageDto {
    private Long id;
    private String path;
    private String filename;
    private Long postId;

    public Image toEntity(Post post) {
        return Image.builder()
                .post(post)
                .filename(UUID.randomUUID().toString())
                .path("ceos/resources/"+filename)
                .build();
    }

    public static ImageDto of (Image image){
        return ImageDto.builder()
                .id(image.getId())
                .filename(image.getFilename())
                .path(image.getPath())
                .postId(image.getPost().getId())
                .build();
    }
}
