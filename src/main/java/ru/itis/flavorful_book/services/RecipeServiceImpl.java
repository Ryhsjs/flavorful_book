package ru.itis.flavorful_book.services;

import ru.itis.flavorful_book.DTO.IngredientDTO;
import ru.itis.flavorful_book.DTO.RecipeInfoDTO;
import ru.itis.flavorful_book.DTO.RecipePreviewDTO;
import ru.itis.flavorful_book.DTO.RecipeSaveDTO;
import ru.itis.flavorful_book.exceptions.IllegalRecipeArgumentException;
import ru.itis.flavorful_book.models.*;
import ru.itis.flavorful_book.repositories.CategoryRepository;
import ru.itis.flavorful_book.repositories.IngredientRecipeRepository;
import ru.itis.flavorful_book.repositories.RecipeRepository;
import ru.itis.flavorful_book.repositories.UserRepository;
import ru.itis.flavorful_book.util.validation.Validator;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final IngredientRecipeRepository ingredientRecipeRepository;

    private final Validator validator;

    public RecipeServiceImpl(RecipeRepository recipeRepository, UserRepository userRepository,
                             CategoryRepository categoryRepository,
                             IngredientRecipeRepository ingredientRecipeRepository, Validator validator) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientRecipeRepository = ingredientRecipeRepository;
        this.validator = validator;
    }

    @Override
    public Recipe save(RecipeSaveDTO recipeSaveDTO) {
        validateRecipeData(recipeSaveDTO.title(), recipeSaveDTO.instructions(), recipeSaveDTO.activeCookingTime(),
                recipeSaveDTO.totalCookingTime(), recipeSaveDTO.servings());

        Recipe recipe = new Recipe(recipeSaveDTO.id(), recipeSaveDTO.title(), recipeSaveDTO.description(),
                recipeSaveDTO.instructions(), recipeSaveDTO.activeCookingTime(), recipeSaveDTO.totalCookingTime(),
                recipeSaveDTO.servings(), recipeSaveDTO.imageUrl(), null, null,
                recipeSaveDTO.userId(), null, null);

        if (recipe.getId() == null || !recipeRepository.existsById(recipe.getId())) {
            return recipeRepository.save(recipe);
        }
        if (recipeRepository.update(recipe))
            return recipeRepository.findById(recipe.getId());
        else
            throw new IllegalArgumentException("Что-то пошло не так при сохранение рецепта");
    }

    @Override
    public void updateViews(Long id) {
        validator.validateRecipeExistence(id);
        recipeRepository.updateViews(id);
    }

    @Override
    public boolean deleteById(Long id) {
        validator.validateRecipeExistence(id);
        return recipeRepository.deleteById(id);
    }

    @Override
    public RecipeSaveDTO findByIdSaveDTO(Long id) {
        validator.validateRecipeExistence(id);
        Recipe recipe = recipeRepository.findById(id);
        List<Long> categories = categoryRepository.findAllByRecipeId(id).stream().map(Category::getId).toList();
        List<IngredientDTO> ingredients = ingredientRecipeRepository.findAll(id).stream()
                .map(ingredientRecipe -> new IngredientDTO(
                        ingredientRecipe.getId(),
                        ingredientRecipe.getQuantity(),
                        ingredientRecipe.getUnit().toString(),
                        ingredientRecipe.getNotes())
                ).toList();
        return new RecipeSaveDTO(id, recipe.getTitle(), recipe.getDescription(), recipe.getInstructions(),
                recipe.getActiveCookingTime(), recipe.getTotalCookingTime(), recipe.getServings(),
                recipe.getImageUrl(), recipe.getUserId(), categories, ingredients);
    }

    @Override
    public RecipeInfoDTO findByIdInfoDTO(Long id) {
        validator.validateRecipeExistence(id);
        return toRecipeInfoDTO(recipeRepository.findById(id));
    }

    @Override
    public List<RecipePreviewDTO> findAll() {
        List<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream().map(this::toRecipePreviewDTO).collect(Collectors.toList());
    }

    @Override
    public List<RecipePreviewDTO> findAll(List<Long> ingredients, boolean isStrictMatchIngredients,
                                          List<Long> categories, boolean isStrictMatchCategories,
                                          Integer activeStart, Integer activeEnd, Integer totalStart, Integer totalEnd) {

        List<Recipe> recipes = recipeRepository.findAll(ingredients, isStrictMatchIngredients,
                categories, isStrictMatchCategories, getTimeStart(activeStart), getTimeEnd(activeEnd),
                getTimeStart(totalStart), getTimeEnd(totalEnd));

        return recipes.stream().map(this::toRecipePreviewDTO).collect(Collectors.toList());
    }

    @Override
    public List<RecipePreviewDTO> findManyByUserFavorites(Long userId) {
        validator.validateUserExistence(userId);
        List<Recipe> recipes = recipeRepository.findAllByUserFavorites(userId);
        return recipes.stream().map(this::toRecipePreviewDTO).collect(Collectors.toList());
    }

    @Override
    public List<RecipePreviewDTO> findManyByUserId(Long userId) {
        validator.validateUserExistence(userId);
        List<Recipe> recipes = recipeRepository.findAllByUserId(userId);
        return recipes.stream().map(this::toRecipePreviewDTO).collect(Collectors.toList());
    }

    @Override
    public void addToFavorites(Long userId, Long recipeId) {
        recipeRepository.addToFavorites(userId, recipeId);
    }

    @Override
    public void deleteFromFavorites(Long userId, Long recipeId) {
        recipeRepository.deleteFromFavorites(userId, recipeId);
    }

    @Override
    public boolean isInFavorites(Long userId, Long recipeId) {
        try {
            return recipeRepository.isInFavorites(userId, recipeId);
        } catch (RuntimeException ignored) {
            System.out.println("sdsad");
            return false;
        }
    }

    private RecipeInfoDTO toRecipeInfoDTO(Recipe recipe) {
        User user = userRepository.findById(recipe.getUserId());
        return new RecipeInfoDTO(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getInstructions(),
                recipe.getActiveCookingTime(),
                recipe.getTotalCookingTime(),
                recipe.getServings(),
                recipe.getImageUrl(),
                recipe.getCreatedAt(),
                recipe.getUpdateAt(),
                recipe.getUserId(),
                user.getUsername(),
                user.getAvatarUrl(),
                recipe.getViews(),
                userRepository.countByFavorites(recipe.getId()),
                recipe.getRating()
        );
    }

    private RecipePreviewDTO toRecipePreviewDTO(Recipe recipe) {
        User user = userRepository.findById(recipe.getUserId());

        return new RecipePreviewDTO(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getActiveCookingTime(),
                recipe.getTotalCookingTime(),
                recipe.getImageUrl(),
                recipe.getCreatedAt(),
                recipe.getUserId(),
                user.getUsername(),
                recipe.getRating(),
                getRecipeIngredients(recipe.getId())
        );
    }

    private String getRecipeIngredients(Long recipeId) {
        List<IngredientRecipe> ingredients = ingredientRecipeRepository.findAll(recipeId);

        return ingredients.stream()
                .map(Ingredient::getName)
                .map(String::toLowerCase)
                .collect(Collectors.joining(", "));
    }

    private int getTimeStart(Integer timeStart) {
        if (timeStart == null)
            return 0;
        return timeStart;
    }

    private int getTimeEnd(Integer timeEnd) {
        if (timeEnd == null)
            return 1440;
        return timeEnd;
    }

    private void validateRecipeData(String title, String instructions, Integer activeCookingTime,
                                    Integer totalCookingTime, Integer servings) {
        IllegalRecipeArgumentException exception = new IllegalRecipeArgumentException("Ошибка при создании рецепта");

        exception.setTitleState(validator.checkString(title, 1, 255));
        exception.setInstructionsState(validator.checkString(instructions));
        exception.setActiveCookingTimeState(validator.checkNumber(activeCookingTime, 0, 1440));
        exception.setTotalCookingTimeState(validator.checkNumber(totalCookingTime, 0, 1440));
        exception.setServingsState(validator.checkNumber(servings, 0, 100));

        if (activeCookingTime != null && totalCookingTime != null && activeCookingTime > totalCookingTime)
            exception.setCookingTimeState("Активное время готовки не может быть больше общего");


        if (exception.isShouldThrow())
            throw exception;
    }
}
