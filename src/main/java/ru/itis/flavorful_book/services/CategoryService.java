package ru.itis.flavorful_book.services;

import ru.itis.flavorful_book.models.Category;

import java.util.List;

public interface CategoryService {
    void save(Long recipeId, Long category);

    void saveAll(Long recipeId, List<Long> categories);

    void delete(Long recipeId, Long category);

    void deleteAll(Long recipeId, List<Long> categories);

    List<Category> findAll();

    List<Category> findByParentId(Long parentId);

    List<Category> findAllByRecipeId(Long recipeId);
}
