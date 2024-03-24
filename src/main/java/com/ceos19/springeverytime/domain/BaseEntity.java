package com.ceos19.springeverytime.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class BaseEntity {
    @Column(updatable = false)
    private LocalDateTime createDate;
    @Column(updatable = false)
    private LocalDateTime modifyDate;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createDate = now;
        modifyDate = now;
    }

    @PreUpdate
    public void preUpdate() {
        modifyDate = LocalDateTime.now();
    }

    public void setDateForTest() {
        LocalDateTime now = LocalDateTime.now();
        this.createDate = now;
        this.modifyDate = now;
    }
}
