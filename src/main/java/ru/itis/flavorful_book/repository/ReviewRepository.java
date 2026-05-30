package ru.itis.flavorful_book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.flavorful_book.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUser_IdAndRecipe_Id(Long userId, Long recipeId);

    List<Review> findAllByRecipe_Id(Long recipeId);

    boolean existsByUser_IdAndRecipe_Id(Long userId, Long recipeId);

    long countByRecipe_Id(Long recipeId);
}
