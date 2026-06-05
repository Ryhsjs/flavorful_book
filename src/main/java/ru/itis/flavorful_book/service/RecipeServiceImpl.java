package ru.itis.flavorful_book.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.flavorful_book.dto.IngredientDTO;
import ru.itis.flavorful_book.dto.RecipeDTO;
import ru.itis.flavorful_book.dto.RecipeInfoDTO;
import ru.itis.flavorful_book.dto.RecipePreviewDTO;
import ru.itis.flavorful_book.entity.Category;
import ru.itis.flavorful_book.entity.Recipe;
import ru.itis.flavorful_book.entity.User;
import ru.itis.flavorful_book.exception.EntityNotFoundException;
import ru.itis.flavorful_book.form.RecipeForm;
import ru.itis.flavorful_book.repository.RecipeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final CategoryService categoryService;
    private final IngredientRecipeService ingredientRecipeService;

    @PersistenceContext
    private EntityManager em;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             CategoryService categoryService,
                             IngredientRecipeService ingredientRecipeService) {
        this.recipeRepository = recipeRepository;
        this.categoryService = categoryService;
        this.ingredientRecipeService = ingredientRecipeService;
    }

    @Override
    @CacheEvict(value = "recipes", allEntries = true)
    @Transactional
    public Long create(RecipeForm form, Long userId) {
        Recipe recipe = new Recipe();
        recipe.setAuthor(em.getReference(User.class, userId));
        recipe.setCreatedAt(LocalDateTime.now());
        applyForm(recipe, form);
        Long recipeId = recipeRepository.save(recipe).getId();
        categoryService.saveAll(recipeId, form.getCategories());
        ingredientRecipeService.saveAll(recipeId, form.getIngredients());
        return recipeId;
    }

    @Override
    @CacheEvict(value = "recipes", allEntries = true)
    @Transactional
    public void update(Long id, RecipeForm form, Long userId) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Рецепт с id=" + id + " не найден"));
        recipe.setUpdatedAt(LocalDateTime.now());
        applyForm(recipe, form);
        recipeRepository.save(recipe);
        categoryService.saveAll(id, form.getCategories());
        ingredientRecipeService.saveAll(id, form.getIngredients());
    }

    private void applyForm(Recipe recipe, RecipeForm form) {
        recipe.setTitle(form.getTitle());
        recipe.setDescription(form.getDescription());
        recipe.setInstructions(form.getInstructions());
        recipe.setActiveCookingTime(form.getActiveCookingTime());
        recipe.setTotalCookingTime(form.getTotalCookingTime());
        recipe.setServings(form.getServings());
        recipe.setImageUrl(form.getImageUrl());
    }

    @Override
    @Transactional
    public void updateViews(Long id) {
        recipeRepository.incrementViews(id);
    }

    @Override
    @CacheEvict(value = "recipes", allEntries = true)
    @Transactional
    public boolean deleteById(Long id) {
        if (!recipeRepository.existsById(id))
            throw new EntityNotFoundException("Рецепт с id=" + id + " не найден");
        recipeRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public RecipeDTO findByIdDTO(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Рецепт с id=" + id + " не найден"));
        List<Long> categoryIds = categoryService.findAllByRecipeId(id).stream()
                .map(Category::getId).toList();
        List<IngredientDTO> ingredients = ingredientRecipeService.findAllDTOByRecipeId(id);
        return RecipeDTO.from(recipe, categoryIds, ingredients);
    }

    @Override
    @Transactional(readOnly = true)
    public RecipeInfoDTO findByIdInfoDTO(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Рецепт с id=" + id + " не найден"));
        return RecipeInfoDTO.from(recipe, recipeRepository.countFavoritesByRecipeId(id));
    }

    @Override
    @Cacheable("recipes")
    @Transactional(readOnly = true)
    public List<RecipePreviewDTO> findAll() {
        List<Recipe> recipes = recipeRepository.findAllByOrderByCreatedAtDesc();
        Map<Long, String> ingredientNames = ingredientRecipeService.getIngredientNamesByRecipeIds(
                recipes.stream().map(Recipe::getId).toList());
        return recipes.stream()
                .map(r -> RecipePreviewDTO.from(r, ingredientNames.getOrDefault(r.getId(), "")))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipePreviewDTO> findAll(List<Long> ingredients, boolean isStrictMatchIngredients,
                                          List<Long> categories, boolean isStrictMatchCategories,
                                          Integer activeStart, Integer activeEnd,
                                          Integer totalStart, Integer totalEnd) {
        List<Recipe> recipes = recipeRepository.findWithFilters(
                ingredients, isStrictMatchIngredients,
                categories, isStrictMatchCategories,
                timeStart(activeStart), timeEnd(activeEnd),
                timeStart(totalStart), timeEnd(totalEnd));
        Map<Long, String> ingredientNames = ingredientRecipeService.getIngredientNamesByRecipeIds(
                recipes.stream().map(Recipe::getId).toList());
        return recipes.stream()
                .map(r -> RecipePreviewDTO.from(r, ingredientNames.getOrDefault(r.getId(), "")))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipePreviewDTO> findManyByUserFavorites(Long userId) {
        List<Recipe> recipes = recipeRepository.findAllFavoritesByUser(userId);
        Map<Long, String> ingredientNames = ingredientRecipeService.getIngredientNamesByRecipeIds(
                recipes.stream().map(Recipe::getId).toList());
        return recipes.stream()
                .map(r -> RecipePreviewDTO.from(r, ingredientNames.getOrDefault(r.getId(), "")))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipePreviewDTO> findManyByUserId(Long userId) {
        List<Recipe> recipes = recipeRepository.findAllByAuthor_IdOrderByCreatedAtDesc(userId);
        Map<Long, String> ingredientNames = ingredientRecipeService.getIngredientNamesByRecipeIds(
                recipes.stream().map(Recipe::getId).toList());
        return recipes.stream()
                .map(r -> RecipePreviewDTO.from(r, ingredientNames.getOrDefault(r.getId(), "")))
                .toList();
    }

    @Override
    @Transactional
    public void addToFavorites(Long userId, Long recipeId) {
        recipeRepository.addToFavorites(userId, recipeId);
    }

    @Override
    @Transactional
    public void deleteFromFavorites(Long userId, Long recipeId) {
        recipeRepository.removeFromFavorites(userId, recipeId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isInFavorites(Long userId, Long recipeId) {
        return recipeRepository.isInFavorites(userId, recipeId);
    }

    private int timeStart(Integer value) {
        return value == null ? 0 : value;
    }

    private int timeEnd(Integer value) {
        return value == null ? 1440 : value;
    }

}
