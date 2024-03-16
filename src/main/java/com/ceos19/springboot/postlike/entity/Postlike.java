package com.ceos19.springboot.postlike.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "post_like")
public class Postlike {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long postLikeId;


}
