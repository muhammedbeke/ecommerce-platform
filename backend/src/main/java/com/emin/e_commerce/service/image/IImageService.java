package com.emin.e_commerce.service.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.emin.e_commerce.dto.ImageDto;
import com.emin.e_commerce.model.Image;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file, Long imageId);
}
