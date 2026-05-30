package ru.itis.flavorful_book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.flavorful_book.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query(value = "SELECT COUNT(*) FROM favorites WHERE recipe_id = :recipeId", nativeQuery = true)
    long countFavoritesByRecipeId(@Param("recipeId") Long recipeId);
}
