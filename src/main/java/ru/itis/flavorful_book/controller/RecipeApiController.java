package ru.itis.flavorful_book.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.flavorful_book.form.RecipeForm;
import ru.itis.flavorful_book.security.CustomeUserDetails;
import ru.itis.flavorful_book.service.RecipeService;
import ru.itis.flavorful_book.util.ValidationUtils;

import java.util.Map;

@RestController
@RequestMapping("/recipes")
public class RecipeApiController {

    private final RecipeService recipeService;

    public RecipeApiController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> create(
            @Valid @RequestBody RecipeForm form,
            BindingResult errors,
            @AuthenticationPrincipal CustomeUserDetails currentUser) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.extractFieldErrors(errors));
        }
        Long id = recipeService.create(form, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", id));
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody RecipeForm form,
            BindingResult errors,
            @AuthenticationPrincipal CustomeUserDetails currentUser) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.extractFieldErrors(errors));
        }
        if (!recipeService.findByIdInfoDTO(id).userId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        recipeService.update(id, form, currentUser.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomeUserDetails currentUser) {
        if (!recipeService.findByIdInfoDTO(id).userId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        recipeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/favorites")
    public ResponseEntity<Void> addToFavorites(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomeUserDetails currentUser) {
        recipeService.addToFavorites(currentUser.getId(), id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/favorites")
    public ResponseEntity<Void> removeFromFavorites(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomeUserDetails currentUser) {
        recipeService.deleteFromFavorites(currentUser.getId(), id);
        return ResponseEntity.ok().build();
    }
}
