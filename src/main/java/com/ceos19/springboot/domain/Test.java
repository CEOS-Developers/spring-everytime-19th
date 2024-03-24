package com.ceos19.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;

@Entity
@Getter
public class Test {

    @Id
    private Long id;
    private String name;
}