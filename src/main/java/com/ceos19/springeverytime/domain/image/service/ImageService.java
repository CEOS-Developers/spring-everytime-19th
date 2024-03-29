package com.ceos19.springeverytime.domain.image.service;

import com.ceos19.springeverytime.domain.image.domain.Image;
import com.ceos19.springeverytime.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    @Transactional
    public Image upload(Image image) {
        return imageRepository.save(image);
    }

    @Transactional
    public void delete(Image image) {
        imageRepository.delete(image);
    }
}
