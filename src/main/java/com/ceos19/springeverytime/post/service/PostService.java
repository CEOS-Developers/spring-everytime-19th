package com.ceos19.springeverytime.post.service;

import com.ceos19.springeverytime.Image.repository.ImageRepository;
import com.ceos19.springeverytime.Image.domain.Image;
import com.ceos19.springeverytime.post.domain.Post;
import com.ceos19.springeverytime.post.dto.RequestPostDto;
import com.ceos19.springeverytime.post.dto.ResponsePostDto;
import com.ceos19.springeverytime.post.repository.PostRepository;
import com.ceos19.springeverytime.postcategory.domain.PostCategory;
import com.ceos19.springeverytime.postcategory.repository.PostCategoryRepository;
import com.ceos19.springeverytime.user.domain.User;
import com.ceos19.springeverytime.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private PostRepository postRepository;
    private UserRepository userRepository;
    private PostCategoryRepository postCategoryRepository;
    private ImageRepository imageRepository;

    @Transactional
    public void createPost(RequestPostDto postDto) throws NotFoundException {
        User user = userRepository.findUserById(postDto.getUserId())
                .orElseThrow(NotFoundException::new);
        PostCategory category = postCategoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(NotFoundException::new);
        List<Image> images = imageRepository.findImagesByImageIds(postDto.getImageId());

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

    public List<ResponsePostDto> getPostByTitle(String title) {
        return postRepository.findByTitleContaining(title).stream()
                .map(ResponsePostDto::of)
                .toList();
    }

    public void updatePost(RequestPostDto request) {
        Post post = postRepository.findPostById(request.getId()).orElseThrow();
        List<Image> resultList = imageRepository.findImagesByImageIds(request.getImageId());
        post.update(request , resultList );
    }
}
