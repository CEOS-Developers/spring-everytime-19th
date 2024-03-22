package com.ceos19.everytime.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class University {
    public static final int MAX_NAME_LENGTH = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "university_id")
    private Long id;

    @Column(nullable = false, length = 15)
    private String name;

    @Builder
    public University(String name){
        this.name = name;
    }

    public void changeName(String name){
        this.name = name;
    }

    private boolean validateName(String name){
        if(name.isEmpty() || name.length()> MAX_NAME_LENGTH)
            return false;
        return true;
    }

}
