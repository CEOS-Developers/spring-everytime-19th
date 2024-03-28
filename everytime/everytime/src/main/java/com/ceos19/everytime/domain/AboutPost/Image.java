package com.ceos19.everytime.domain.AboutPost;


import com.ceos19.everytime.domain.AboutPost.Post;
import com.ceos19.everytime.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Image extends BaseTimeEntity {
    @Id
    @Column(name="image_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long imageId;

    @Column(name="file_title", nullable = false)
    private String fileTitle;

    @Column(name="file_path", nullable = false)
    private String filePath;

    @Column(name="file_size", nullable = false)
    private Long fileSize;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

}
