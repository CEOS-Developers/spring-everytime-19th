package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @NotNull
    private long imageIdx;

    @NotNull
    private String originalFileName;

    @NotNull
    private String storedFileName;

    private long fileSize;

    @ManyToOne(fetch = LAZY)
    private Post post;
}
