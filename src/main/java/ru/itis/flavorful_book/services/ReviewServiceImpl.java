package ru.itis.flavorful_book.services;

import ru.itis.flavorful_book.DTO.ReviewDTO;
import ru.itis.flavorful_book.models.Review;
import ru.itis.flavorful_book.models.User;
import ru.itis.flavorful_book.repositories.ReviewRepository;
import ru.itis.flavorful_book.repositories.UserRepository;
import ru.itis.flavorful_book.util.validation.Validator;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    private final Validator validator;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, Validator validator) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @Override
    public Review save(Long userId, Long recipeId, Integer rating, String comment) {
        validator.validateUserExistence(userId);
        validator.validateRecipeExistence(recipeId);

        try {
            validator.validateNumber(rating, 1, 5);
        } catch (IllegalArgumentException e) {
            rating = 5;
        }

        if (reviewRepository.existsByUserIdRecipeId(userId, recipeId)) {
            Long id = reviewRepository.findByUserIdRecipeId(userId, recipeId).getId();
            if (update(id, rating, comment))
                return reviewRepository.findById(id);
            throw new IllegalArgumentException("Что-то пошло не так при сохранении отзыва");
        }

        Review review = new Review(null, userId, recipeId, rating, comment, null, null);
        return reviewRepository.save(review);
    }

    @Override
    public boolean update(Long id, Integer rating, String comment) {
        validateReviewExistence(id);
        validator.validateNumber(rating, 1, 5);

        Review review = reviewRepository.findById(id);
        review.setRating(rating);
        review.setComment(comment);
        return reviewRepository.update(review);
    }

    @Override
    public boolean deleteById(Long id) {
        validateReviewExistence(id);

        return reviewRepository.deleteById(id);
    }

    @Override
    public ReviewDTO findById(Long id) {
        validateReviewExistence(id);

        Review review = reviewRepository.findById(id);
        return toReviewDTO(review);
    }

    @Override
    public ReviewDTO findByUserIdRecipeId(Long userId, Long recipeId) {
        try {
            Review review = reviewRepository.findByUserIdRecipeId(userId, recipeId);
            return toReviewDTO(review);
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override
    public List<ReviewDTO> findAllByRecipeId(Long recipeId) {
        validator.validateRecipeExistence(recipeId);

        List<Review> reviews = reviewRepository.findAllByRecipeId(recipeId);
        return reviews.stream().map(this::toReviewDTO).collect(Collectors.toList());
    }

    private ReviewDTO toReviewDTO(Review review) {
        User user = userRepository.findById(review.getUserId());
        return new ReviewDTO(
                review.getId(),
                review.getUserId(),
                review.getRecipeId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getUpdatedAt(),
                user.getUsername(),
                user.getAvatarUrl()
        );
    }

    private void validateReviewExistence(Long id) {
        if (!reviewRepository.existsById(id))
            throw new IllegalArgumentException("Такого отзыва не существует");

    }
}

