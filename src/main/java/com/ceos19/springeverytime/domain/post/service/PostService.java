package com.ceos19.springeverytime.domain.post.service;

import static com.ceos19.springeverytime.global.exception.ExceptionCode.NOT_FOUND_CATEGORY_ID;
import static com.ceos19.springeverytime.global.exception.ExceptionCode.NOT_FOUND_POST_ID;

import com.ceos19.springeverytime.domain.category.domain.Category;
import com.ceos19.springeverytime.domain.post.domain.Post;
import com.ceos19.springeverytime.domain.post.dto.request.PostCreateRequest;
import com.ceos19.springeverytime.domain.post.dto.response.PostDetailResponse;
import com.ceos19.springeverytime.domain.user.domain.User;
import com.ceos19.springeverytime.global.exception.BadRequestException;
import com.ceos19.springeverytime.domain.category.repository.CategoryRepository;
import com.ceos19.springeverytime.domain.post.repository.PostRepository;
import com.ceos19.springeverytime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public PostDetailResponse getPostDetail(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_POST_ID));
        return PostDetailResponse.from(findPost);
    }

    @Transactional
    public Post save(Long categoryId, PostCreateRequest request) {
        User user = userRepository.findByLoginId("test").orElse(
                userRepository.save(new User("test","test", "test", "test", "test", "20", "test@example.com"))
        );
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new BadRequestException(NOT_FOUND_CATEGORY_ID)
        );
        Post post = new Post(request.getTitle(), request.getContent(), request.isAnonymous(), user, category);
        return postRepository.save(post);
    }

    @Transactional
    public void delete(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new BadRequestException(NOT_FOUND_POST_ID);
        }

        postRepository.deleteById(postId);
    }
}
