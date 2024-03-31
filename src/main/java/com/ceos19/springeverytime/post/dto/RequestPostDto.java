package com.ceos19.springeverytime.post.dto;

import com.ceos19.springeverytime.Image.domain.Image;
import com.ceos19.springeverytime.post.domain.Post;
import com.ceos19.springeverytime.postcategory.domain.PostCategory;
import com.ceos19.springeverytime.user.domain.User;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestPostDto {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private Long categoryId;
    private List<Long> imageId;

    public Post toEntity(User user, PostCategory postCategory, List<Image> images) {
        return Post.builder()
                .title(title)
                .content(content)
                .author(user)
                .image(images)
                .category(postCategory)
                .likeCount(0)
                .build();
    }
}
