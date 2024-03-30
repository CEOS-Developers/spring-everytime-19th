package com.ceos19.everytime.exception;


import lombok.Getter;

@Getter
public class PostNotFoundException extends RuntimeException {
    private final String postId;

    public PostNotFoundException(String postId) {
        super(String.format("Post with ID '%s' not found", postId));
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }
}