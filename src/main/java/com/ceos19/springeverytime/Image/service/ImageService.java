package com.ceos19.springeverytime.Image.service;


import com.ceos19.springeverytime.Image.dto.ImageDto;
import com.ceos19.springeverytime.Image.repository.ImageRepository;
import com.ceos19.springeverytime.post.domain.Post;
import com.ceos19.springeverytime.post.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final PostService postService;

    @Transactional
    public void createImage(Long postId, ImageDto imageDto){
        Post post = postService.getPost(postId);
        imageRepository.save(imageDto.toEntity(post));
    }
    @Transactional
    public void deleteImage(Long imageId){
        imageRepository.deleteById(imageId);
    }

    public List<ImageDto> getImagesByPostId(Long postId){
        return imageRepository.findByPostId(postId).stream()
                .map(ImageDto::of)
                .toList();
    }
}
