package ru.itis.flavorful_book.service;

import jakarta.servlet.http.Part;

public interface ImageService {
    String saveRecipeImage(Part imagePart);

    String saveUserAvatar(Part imagePart);
}
