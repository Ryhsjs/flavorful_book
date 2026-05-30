package ru.itis.flavorful_book.service;

import ru.itis.flavorful_book.entity.Category;
import ru.itis.flavorful_book.repository.CategoryRepository;
import ru.itis.flavorful_book.util.validation.Validator;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final Validator validator;

    public CategoryServiceImpl(CategoryRepository categoryRepository, Validator validator) {
        this.categoryRepository = categoryRepository;
        this.validator = validator;
    }

    @Override
    public void save(Long recipeId, Long category) {
         try {
             categoryRepository.save(recipeId, category);
        } catch (RuntimeException ignored) {}
    }

    @Override
    public void saveAll(Long recipeId, List<Long> categories) {
        validator.validateRecipeExistence(recipeId);

        List<Long> recipeCategories = new java.util.ArrayList<>(categoryRepository.findAllByRecipeId(recipeId).stream()
                .map(Category::getId).toList());

        for (Long categoryId: categories) {
            recipeCategories.remove(categoryId);

            save(recipeId, categoryId);
        }

        deleteAll(recipeId, recipeCategories);
    }

    @Override
    public void delete(Long recipeId, Long category) {
        try {
             categoryRepository.delete(recipeId, category);
        } catch (RuntimeException ignored) {}
    }

    @Override
    public void deleteAll(Long recipeId, List<Long> categories) {
        validator.validateRecipeExistence(recipeId);

        for (Long categoryId: categories) {
            delete(recipeId, categoryId);
        }
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findByParentId(Long parentId) {
        validateCategoryExistence(parentId);
        return categoryRepository.findAllByParentId(parentId);
    }

    @Override
    public List<Category> findAllByRecipeId(Long recipeId) {
        validator.validateRecipeExistence(recipeId);
        return categoryRepository.findAllByRecipeId(recipeId);
    }

    private void validateCategoryExistence(Long categoryId) {
        if (!categoryRepository.exists(categoryId))
            throw new IllegalArgumentException("Такой категории не существует");
    }
}
