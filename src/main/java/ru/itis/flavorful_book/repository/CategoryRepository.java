package ru.itis.flavorful_book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.flavorful_book.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByParentId(Long parentId);

    @Query("SELECT c FROM Recipe r JOIN r.categories c WHERE r.id = :recipeId")
    List<Category> findAllByRecipeId(@Param("recipeId") Long recipeId);
}
