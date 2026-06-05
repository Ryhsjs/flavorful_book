package ru.itis.flavorful_book.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String saveRecipeImage(MultipartFile file);

    String saveUserAvatar(MultipartFile file);
}
