package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity{  //게시물
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @NotBlank(message = "제목은 비어있을 수 없습니다.")
    private String title;
    @NotNull(message = "내용은 null일수 없습니다.")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL)
    private List<Comment> comments = new ArrayList<>();
}
