package ru.itis.flavorful_book.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.flavorful_book.dto.ReviewDTO;
import ru.itis.flavorful_book.entity.Recipe;
import ru.itis.flavorful_book.entity.Review;
import ru.itis.flavorful_book.entity.User;
import ru.itis.flavorful_book.exception.EntityNotFoundException;
import ru.itis.flavorful_book.repository.ReviewRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @PersistenceContext
    private EntityManager em;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional
    public void save(Long userId, Long recipeId, Integer rating, String comment) {
        Review review = new Review();
        review.setUser(em.getReference(User.class, userId));
        review.setRecipe(em.getReference(Recipe.class, recipeId));
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }

    @Override
    @Transactional
    public boolean update(Long id, Integer rating, String comment) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Отзыв с id=" + id + " не найден"));
        review.setRating(rating);
        review.setComment(comment);
        review.setUpdatedAt(LocalDateTime.now());
        reviewRepository.save(review);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        if (!reviewRepository.existsById(id))
            throw new EntityNotFoundException("Отзыв с id=" + id + " не найден");
        reviewRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDTO findById(Long id) {
        return reviewRepository.findById(id)
                .map(ReviewDTO::from)
                .orElseThrow(() -> new EntityNotFoundException("Отзыв с id=" + id + " не найден"));
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDTO findByUserIdRecipeId(Long userId, Long recipeId) {
        return reviewRepository.findByUser_IdAndRecipe_Id(userId, recipeId)
                .map(ReviewDTO::from)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> findAllByRecipeId(Long recipeId) {
        return reviewRepository.findAllByRecipe_Id(recipeId).stream()
                .map(ReviewDTO::from)
                .toList();
    }
}
