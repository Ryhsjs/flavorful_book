package ru.itis.flavorful_book.exception;

public class ImageSaveException extends RuntimeException {
    public ImageSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
