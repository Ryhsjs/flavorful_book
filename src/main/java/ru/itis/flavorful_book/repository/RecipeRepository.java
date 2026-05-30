package ru.itis.flavorful_book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.flavorful_book.entity.Recipe;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long>, RecipeRepositoryCustom {

    List<Recipe> findAllByOrderByCreatedAtDesc();

    List<Recipe> findAllByAuthor_IdOrderByCreatedAtDesc(Long authorId);

    @Query(value = """
            SELECT r.* FROM recipes r
            INNER JOIN favorites f ON r.id = f.recipe_id
            WHERE f.user_id = :userId
            ORDER BY f.saved_at DESC
            """, nativeQuery = true)
    List<Recipe> findAllFavoritedByUser(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE Recipe r SET r.views = r.views + 1 WHERE r.id = :id")
    void incrementViews(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO favorites (user_id, recipe_id) VALUES (:userId, :recipeId)", nativeQuery = true)
    void addToFavorites(@Param("userId") Long userId, @Param("recipeId") Long recipeId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM favorites WHERE user_id = :userId AND recipe_id = :recipeId", nativeQuery = true)
    void removeFromFavorites(@Param("userId") Long userId, @Param("recipeId") Long recipeId);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM favorites WHERE user_id = :userId AND recipe_id = :recipeId)", nativeQuery = true)
    boolean isInFavorites(@Param("userId") Long userId, @Param("recipeId") Long recipeId);
}
