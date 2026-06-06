package ru.itis.flavorful_book.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.flavorful_book.dto.IngredientDTO;
import ru.itis.flavorful_book.entity.Ingredient;
import ru.itis.flavorful_book.entity.IngredientRecipe;
import ru.itis.flavorful_book.entity.Recipe;
import ru.itis.flavorful_book.entity.enums.Unit;
import ru.itis.flavorful_book.exception.EntityNotFoundException;
import ru.itis.flavorful_book.repository.IngredientRecipeRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IngredientRecipeServiceImpl implements IngredientRecipeService {

    private final IngredientRecipeRepository ingredientRecipeRepository;
    private final IngredientService ingredientService;

    public IngredientRecipeServiceImpl(IngredientRecipeRepository ingredientRecipeRepository,
                                       IngredientService ingredientService) {
        this.ingredientRecipeRepository = ingredientRecipeRepository;
        this.ingredientService = ingredientService;
    }

    @Override
    @Transactional
    public void save(Recipe recipe, IngredientDTO ingredient) {
        Unit unit = ingredient.unit();
        ingredientRecipeRepository.findByRecipe_IdAndIngredient_Id(recipe.getId(), ingredient.id())
                .ifPresentOrElse(
                        ir -> {
                            ir.setQuantity(ingredient.quantity());
                            ir.setUnit(unit);
                            ir.setNotes(ingredient.notes());
                            ingredientRecipeRepository.save(ir);
                        },
                        () -> {
                            IngredientRecipe ir = new IngredientRecipe();
                            ir.setRecipe(recipe);
                            ir.setIngredient(ingredientService.findById(ingredient.id()));
                            ir.setQuantity(ingredient.quantity());
                            ir.setUnit(unit);
                            ir.setNotes(ingredient.notes());
                            ingredientRecipeRepository.save(ir);
                        }
                );
    }

    @Override
    @Transactional
    public void saveAll(Recipe recipe, List<IngredientDTO> ingredients) {
        Map<Long, IngredientRecipe> existing = ingredientRecipeRepository.findAllByRecipe_Id(recipe.getId())
                .stream()
                .collect(Collectors.toMap(ir -> ir.getIngredient().getId(), ir -> ir));

        Set<Long> incomingIds = new HashSet<>();
        List<IngredientRecipe> toSave = new ArrayList<>();

        // batch-load all needed ingredients at once
        Set<Long> newIngredientIds = ingredients.stream()
                .map(IngredientDTO::id)
                .filter(id -> !existing.containsKey(id))
                .collect(Collectors.toSet());
        Map<Long, Ingredient> ingredientMap = ingredientService.findAllByIds(newIngredientIds).stream()
                .collect(Collectors.toMap(Ingredient::getId, i -> i));

        for (IngredientDTO dto : ingredients) {
            incomingIds.add(dto.id());
            IngredientRecipe ir = existing.get(dto.id());
            if (ir == null) {
                ir = new IngredientRecipe();
                ir.setRecipe(recipe);
                ir.setIngredient(ingredientMap.get(dto.id()));
            }
            ir.setQuantity(dto.quantity());
            ir.setUnit(dto.unit());
            ir.setNotes(dto.notes());
            toSave.add(ir);
        }

        ingredientRecipeRepository.saveAll(toSave);

        Set<Long> toDeleteIds = existing.keySet().stream()
                .filter(id -> !incomingIds.contains(id))
                .collect(Collectors.toSet());
        if (!toDeleteIds.isEmpty()) {
            ingredientRecipeRepository.deleteAllByRecipeIdAndIngredientIds(recipe.getId(), toDeleteIds);
        }
    }

    @Override
    @Transactional
    public void delete(Long recipeId, Long ingredientId) {
        ingredientRecipeRepository.deleteByRecipe_IdAndIngredient_Id(recipeId, ingredientId);
    }

    @Override
    @Transactional
    public void deleteAll(Long recipeId, List<Long> ingredients) {
        if (!ingredients.isEmpty()) {
            ingredientRecipeRepository.deleteAllByRecipeIdAndIngredientIds(recipeId, new HashSet<>(ingredients));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientRecipe> findAll(Long recipeId) {
        if (!ingredientRecipeRepository.existsByRecipe_Id(recipeId))
            throw new EntityNotFoundException("Рецепт с id=" + recipeId + " не найден");
        return ingredientRecipeRepository.findAllByRecipe_Id(recipeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientDTO> findAllDTOByRecipeId(Long recipeId) {
        return ingredientRecipeRepository.findAllByRecipe_Id(recipeId).stream()
                .map(IngredientDTO::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, String> getIngredientNamesByRecipeIds(Collection<Long> recipeIds) {
        if (recipeIds.isEmpty()) return Map.of();
        return ingredientRecipeRepository.findAllByRecipeIds(recipeIds).stream()
                .collect(Collectors.groupingBy(
                        ir -> ir.getRecipe().getId(),
                        Collectors.mapping(
                                ir -> ir.getIngredient().getName().toLowerCase(),
                                Collectors.joining(", ")
                        )
                ));
    }
}
