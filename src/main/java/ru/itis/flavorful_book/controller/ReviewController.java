package ru.itis.flavorful_book.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.flavorful_book.dto.ReviewDTO;
import ru.itis.flavorful_book.form.ReviewForm;
import ru.itis.flavorful_book.security.CustomeUserDetails;
import ru.itis.flavorful_book.service.ReviewService;
import ru.itis.flavorful_book.util.ValidationUtils;

import java.util.List;

@RestController
@RequestMapping("/recipes/{recipeId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> findAll(@PathVariable Long recipeId) {
        return ResponseEntity.ok(reviewService.findAllByRecipeId(recipeId));
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> save(
            @PathVariable Long recipeId,
            @Valid @RequestBody ReviewForm form,
            BindingResult errors,
            @AuthenticationPrincipal CustomeUserDetails currentUser) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.extractFieldErrors(errors));
        }
        reviewService.save(currentUser.getId(), recipeId, form.getRating(), form.getComment());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<?> update(
            @PathVariable Long recipeId,
            @PathVariable Long id,
            @Valid @RequestBody ReviewForm form,
            BindingResult errors,
            @AuthenticationPrincipal CustomeUserDetails currentUser) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.extractFieldErrors(errors));
        }
        ReviewDTO review = reviewService.findById(id);
        if (!review.userId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        reviewService.update(id, form.getRating(), form.getComment());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long recipeId,
            @PathVariable Long id,
            @AuthenticationPrincipal CustomeUserDetails currentUser) {
        ReviewDTO review = reviewService.findById(id);
        if (!review.userId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        reviewService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
