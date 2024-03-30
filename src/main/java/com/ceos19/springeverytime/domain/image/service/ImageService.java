package com.ceos19.springeverytime.domain.image.service;

import com.ceos19.springeverytime.domain.image.domain.Image;
import com.ceos19.springeverytime.domain.image.repository.ImageRepository;
import com.ceos19.springeverytime.domain.post.domain.Post;
import com.ceos19.springeverytime.domain.post.repository.PostRepository;
import com.ceos19.springeverytime.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.ceos19.springeverytime.global.exception.ExceptionCode.NOT_FOUND_POST_ID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final PostRepository postRepository;

    @Transactional
    public Image upload(MultipartFile image) {
        Image newImage = new Image(Objects.requireNonNull(image.getOriginalFilename()));
        return imageRepository.save(newImage);
    }

    @Transactional
    public void delete(Long imageId) {
        imageRepository.deleteById(imageId);
    }
}
