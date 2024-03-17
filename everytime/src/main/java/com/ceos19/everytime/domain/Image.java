package com.ceos19.everytime.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name="Image")
@Getter
@Setter
public class Image {
    @Id
    @Column(name="image_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long image_id;

    @Column(name="file_title", nullable = false)
    private String file_title;

    @Column(name="file_path", nullable = false)
    private String file_path;

    @Column(name="file_size", nullable = false)
    private Long file_size;

    @Column(name="upload_at", nullable = false)
    private Timestamp upload_at;

    @Column(name="updated_at", nullable = false)
    private Timestamp updated_at;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

}
