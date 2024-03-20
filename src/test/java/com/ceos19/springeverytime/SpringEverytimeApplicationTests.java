package com.ceos19.springeverytime;

import com.ceos19.springeverytime.domain.Category;
import com.ceos19.springeverytime.domain.Post;
import com.ceos19.springeverytime.domain.User;
import com.ceos19.springeverytime.domain.like.PostLike;
import com.ceos19.springeverytime.repository.CategoryRepository;
import com.ceos19.springeverytime.repository.LikeRepository;
import com.ceos19.springeverytime.repository.PostRepository;
import com.ceos19.springeverytime.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

@SpringBootTest
@Transactional
class SpringEverytimeApplicationTests {
    @Test
    void contextLoads() {
    }
}
