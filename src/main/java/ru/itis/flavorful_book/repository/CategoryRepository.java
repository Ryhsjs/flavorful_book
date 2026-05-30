package ru.itis.flavorful_book.repository;

import ru.itis.flavorful_book.entity.Category;

import java.util.List;

public interface CategoryRepository {
    boolean save(Long recipeId, Long categoryId);

    boolean delete(Long recipeId, Long categoryId);

    Category findById(Long categoryId);

    List<Category> findAll();

    List<Category> findAllByParentId(Long parentId);

    List<Category> findAllByRecipeId(Long recipeId);

    boolean exists(Long categoryId);

    boolean exists(Long recipeId, Long categoryId);
}
