package com.ceos19.springeverytime.common;

import com.ceos19.springeverytime.domain.category.domain.Category;
import com.ceos19.springeverytime.domain.Comment;
import com.ceos19.springeverytime.domain.post.domain.Post;
import com.ceos19.springeverytime.domain.user.domain.User;

public class EntityGenerator {
    public static User generateUser(String loginId) {
        return new User(
                loginId,
            "1234",
            "nickname",
            "kim",
            "computer",
            "20",
            "test@example.com"
        );
    }

    public static Category generateCategory(User manager) {
        return new Category("자유게시판", "자유롭게 이야기 해봐요", manager);
    }

    public static Post generatePost(User author, Category category) {
        return new Post("첫번째 글", "첫번째 글입니다.", true, author, category);
    }

    public static Comment generateComment(User author, Post post) {
        return new Comment("content", true, author, post);
    }
}
