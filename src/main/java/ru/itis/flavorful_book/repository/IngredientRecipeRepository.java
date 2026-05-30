package ru.itis.flavorful_book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.flavorful_book.entity.IngredientRecipe;

import java.util.List;
import java.util.Optional;

public interface IngredientRecipeRepository extends JpaRepository<IngredientRecipe, Long> {

    List<IngredientRecipe> findAllByRecipe_Id(Long recipeId);

    Optional<IngredientRecipe> findByRecipe_IdAndIngredient_Id(Long recipeId, Long ingredientId);

    boolean existsByRecipe_IdAndIngredient_Id(Long recipeId, Long ingredientId);

    @Transactional
    @Modifying
    void deleteByRecipe_IdAndIngredient_Id(Long recipeId, Long ingredientId);

    @Transactional
    @Modifying
    @Query("DELETE FROM IngredientRecipe ir WHERE ir.recipe.id = :recipeId")
    void deleteAllByRecipeId(@Param("recipeId") Long recipeId);
}
