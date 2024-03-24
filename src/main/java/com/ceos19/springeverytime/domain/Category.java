package com.ceos19.springeverytime.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Getter
public class Category extends BaseEntity {
    @Id
    @GeneratedValue
    private Long categoryId;

    @NonNull
    @Column(length = 100, nullable = false)
    private String name;

    @NonNull
    @Column(length = 300, nullable = false)
    private String description;

    @NonNull
    @ManyToOne
    private User manager;

    @NonNull
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();

    /**
     * 비즈니스 로직
     * */

    // 관리자 변경
    public void changeManager(User newManager) {
        this.manager = newManager;
    }
}
