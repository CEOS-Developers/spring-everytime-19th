package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "post")
public class Attachment extends BaseTimeEntity{  // 게시물에 달 사진
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "attachment_id")
    private Long id;

    @Column(nullable = false)
    private String originalFileName;
    @Column(nullable = false)
    private String storedPath;

    @Enumerated(STRING)
    private AttachmentType attachmentType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    @Setter(value = PROTECTED)
    private Post post;

    @Builder
    public Attachment(String originFileName, String storePath, AttachmentType attachmentType) {
        this.originalFileName = originFileName;
        this.storedPath = storePath;
        this.attachmentType = attachmentType;
    }
}
