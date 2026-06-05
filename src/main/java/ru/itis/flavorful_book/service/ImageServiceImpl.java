package ru.itis.flavorful_book.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.flavorful_book.exception.ImageSaveException;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private static final String RECIPE_DIR = "recipe";
    private static final String AVATAR_DIR = "avatar";

    private final String uploadDir;

    public ImageServiceImpl(@Value("${app.upload.dir}") String uploadDir) {
        this.uploadDir = uploadDir;
        createDirectories();
    }

    private void createDirectories() {
        new File(uploadDir + File.separator + RECIPE_DIR).mkdirs();
        new File(uploadDir + File.separator + AVATAR_DIR).mkdirs();
    }

    @Override
    public String saveRecipeImage(MultipartFile file) {
        return save(file, RECIPE_DIR);
    }

    @Override
    public String saveUserAvatar(MultipartFile file) {
        return save(file, AVATAR_DIR);
    }

    private String save(MultipartFile file, String subDir) {
        validateFile(file);
        String fileName = UUID.randomUUID().toString().substring(0, 8) + getExtension(file);
        String relativePath = File.separator + subDir + File.separator + fileName;
        try {
            file.transferTo(new File(uploadDir + relativePath));
        } catch (IOException e) {
            throw new ImageSaveException("Не удалось сохранить файл", e);
        }
        return relativePath;
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty())
            throw new IllegalArgumentException("Файл не выбран");
        if (file.getSize() > 5 * 1024 * 1024)
            throw new IllegalArgumentException("Размер файла не должен превышать 5MB");
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/"))
            throw new IllegalArgumentException("Файл не является изображением");
    }

    private String getExtension(MultipartFile file) {
        String name = file.getOriginalFilename();
        if (name != null && name.contains("."))
            return name.substring(name.lastIndexOf("."));
        return ".img";
    }
}
