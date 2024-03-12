package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Email
    private String username;
    @NotBlank
    private String password;
    private int studentNo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @OneToMany(mappedBy = "user",cascade = ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(fetch = LAZY, orphanRemoval = true)
    @JoinColumn(name = "like_id")
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    private List<Message> sendMessages = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<Message> receiveMessages = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();


}
