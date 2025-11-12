package ru.itis.flavorful_book.util.validation;

import ru.itis.flavorful_book.repositories.RecipeRepository;
import ru.itis.flavorful_book.repositories.UserRepository;

public class Validator {
    private final UserRepository userRepository;

    private final RecipeRepository recipeRepository;

    public Validator(UserRepository userRepository, RecipeRepository recipeRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    public String checkString(String str) {
        if (str == null || str.isBlank())
            return "Поле не должно быть пустым";
        return null;
    }

    public String checkString(String str, int maxLen) {
        if (str == null || str.isBlank() || str.length() > maxLen)
            return String.format("Поле должно содержать меньше %d символов", maxLen);
        return null;
    }

    public String checkString(String str, int minLen, int maxLen) {
        if (str == null || str.isBlank() || str.length() < minLen || str.length() > maxLen)
            return String.format("Поле должно содержать больше %d и меньше %d символов", minLen, maxLen);
        return null;
    }

    public String checkNumber(Integer number, int min, int max) {
        if (number == null || number < min || number > max)
            return String.format("Поле должно быть не меньше %d и не больше %d", min, max);
        return null;
    }

    public String checkNumber(Integer number, int min) {
        if (number == null || number < min)
            return String.format("Поле должно быть не меньше %d", min);
        return null;
    }

//    public void validateString(String str){
//        String message = checkString(str);
//        if (message != null)
//            throw new IllegalArgumentException(message);
//    }
//
//    public void validateString(String str, int minLen, int maxLen) {
//        String message = checkString(str, minLen, maxLen);
//        if (message != null)
//            throw new IllegalArgumentException(message);
//    }

    public void validateNumber(Integer number, int min, int max) {
        String message = checkNumber(number, min, max);
        if (message != null)
            throw new IllegalArgumentException(message);
    }

    public void validateNumber(Integer number, int min) {
        String message = checkNumber(number, min);
        if (message != null)
            throw new IllegalArgumentException(message);
    }

    public void validateUserExistence(Long userId) {
        if (!userRepository.existsById(userId))
            throw new IllegalArgumentException("Такого пользователя не существует");
    }

    public void validateRecipeExistence(Long recipeId) {
        if (!recipeRepository.existsById(recipeId))
            throw new IllegalArgumentException("Такого рецепта не существует");
    }
}
