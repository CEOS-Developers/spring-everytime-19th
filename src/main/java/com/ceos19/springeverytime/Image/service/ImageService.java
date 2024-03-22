package com.ceos19.springeverytime.Image.service;


import com.ceos19.springeverytime.Image.domain.Image;
import com.ceos19.springeverytime.Image.repository.ImageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {
    private ImageRepository imageRepository;

    public List<Image> getImagesByImageIds(List<Long> imageIds) {
        return imageRepository.findImagesByImageIds(imageIds)
                .orElseThrow(IllegalAccessError::new);
    }
}
