package com.ceos19.springeverytime.Image.service;


import com.ceos19.springeverytime.Image.domain.Image;
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
    private ImageRepository imageRepository;
    private PostService postService;

    public List<Image> getImagesByImageIds(List<Long> imageIds) {
        return imageRepository.findImagesByImageIds(imageIds)
                .orElseThrow(IllegalAccessError::new);
    }
    @Transactional
    public void createImage(ImageDto imageDto){
        Post post = postService.getPost(imageDto.getPostId());
        imageRepository.save(imageDto.toEntity(post));
    }
    @Transactional
    public void deleteImage(Long imageId){
        imageRepository.deleteById(imageId);
    }
}
