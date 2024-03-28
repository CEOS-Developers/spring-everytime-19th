package com.ceos19.springeverytime.post.service;

import com.ceos19.springeverytime.Image.service.ImageService;
import com.ceos19.springeverytime.Image.domain.Image;
import com.ceos19.springeverytime.post.domain.Post;
import com.ceos19.springeverytime.post.dto.CreatePostDto;
import com.ceos19.springeverytime.post.repository.PostRepository;
import com.ceos19.springeverytime.postcategory.domain.PostCategory;
import com.ceos19.springeverytime.postcategory.service.PostCategoryService;
import com.ceos19.springeverytime.user.domain.User;
import com.ceos19.springeverytime.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private PostRepository postRepository;
    private UserService userService;
    private PostCategoryService postCategoryService;
    private ImageService imageService;

    @Transactional
    public void createPost(CreatePostDto postDto) {
        User user = userService.getUser(postDto.getUserId());
        PostCategory category = postCategoryService.getPostCategoryByCategoryId(postDto.getCategoryId());
        List<Image> images = imageService.getImagesByImageIds(postDto.getImageId());

        postRepository.save(postDto.toEntity(user, category, images));
    }

    public Post getPost(Long postId) {
        return postRepository.findPostById(postId)
                .orElseThrow(IllegalStateException::new);
    }

    @Transactional
    public void deletePost(Long postId) {
        postRepository.deletePostById(postId);
    }

    @Transactional
    public void increaseLike(Long postId){
        Post post = getPost(postId);
        post.increaseLikeCount();
    }
    @Transactional
    public void decreaseLike(Long postId){

        Post post = getPost(postId);
        post.decreaseLikeCount();
    }

}
