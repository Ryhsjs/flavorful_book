package ru.itis.flavorful_book.service;

import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ImageServiceImpl implements ImageService {
    private final String uploadBasePath;

    private final String absolutePath;

    private final String UPLOADS_DIR = "uploads";

    private final String RECIPE_IMAGE_DIR = "recipe";

    private final String AVATAR_IMAGE_DIR = "avatar";

    public ImageServiceImpl(String uploadBasePath, String absolutePath) {
        this.uploadBasePath = uploadBasePath;
        this.absolutePath = absolutePath;
        createUploadDirectories();
    }

    private void createUploadDirectories() {
        String[] directories = {RECIPE_IMAGE_DIR, AVATAR_IMAGE_DIR};
        for (String dir : directories) {
            File directory = new File(uploadBasePath + File.separator + UPLOADS_DIR + File.separator + dir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }

        for (String dir : directories) {
            File directory = new File(absolutePath + File.separator + UPLOADS_DIR + File.separator + dir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }
    }

    @Override
    public String saveRecipeImage(Part imagePart) {

        try {
            validateImageFile(imagePart);

            String fileName = generateFileName(imagePart);

            String fileUrl = generateUrl(RECIPE_IMAGE_DIR, fileName);

            saveFile(imagePart, fileUrl);

            return fileUrl;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String saveUserAvatar(Part imagePart) {
        try {
            validateImageFile(imagePart);

            String fileName = generateFileName(imagePart);

            String fileUrl = generateUrl(AVATAR_IMAGE_DIR, fileName);

            saveFile(imagePart, fileUrl);

            return fileUrl;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void validateImageFile(Part imagePart) {
        if (imagePart.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Размер файла не должен превышать 5MB");
        }

        if (!isSupportedImageType(imagePart.getContentType())) {
            throw new IllegalArgumentException("Файл не является изображением");
        }
    }

    private boolean isSupportedImageType(String contentType) {
        return contentType != null && (
                contentType.split("/")[0].equals("image"));
    }

    private String generateFileName(Part part) {
        String extension = getFileExtension(part);
        return UUID.randomUUID().toString().substring(0, 8) + extension;
    }

    private String generateUrl(String dir, String fileName) {
        return File.separator + UPLOADS_DIR + File.separator + dir + File.separator + fileName;
    }

    private String getFileExtension(Part part) {
        String fileName = part.getSubmittedFileName();
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return ".img";
    }

    private void saveFile(Part part, String fileUrl) throws IOException {
        part.write(uploadBasePath + File.separator + fileUrl);
        part.write(absolutePath + File.separator + fileUrl);
    }
}
