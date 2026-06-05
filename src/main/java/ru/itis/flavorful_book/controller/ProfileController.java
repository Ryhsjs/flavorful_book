package ru.itis.flavorful_book.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.flavorful_book.exception.IllegalUserArgumentException;
import ru.itis.flavorful_book.form.ProfileEditForm;
import ru.itis.flavorful_book.security.CustomeUserDetails;
import ru.itis.flavorful_book.service.RecipeService;
import ru.itis.flavorful_book.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final RecipeService recipeService;

    public ProfileController(UserService userService, RecipeService recipeService) {
        this.userService = userService;
        this.recipeService = recipeService;
    }

    @GetMapping
    public String profilePage(@RequestParam(required = false, defaultValue = "my") String section,
                              @AuthenticationPrincipal CustomeUserDetails currentUser,
                              Model model) {
        populateModel(model, currentUser.getId(), section);
        ProfileEditForm form = new ProfileEditForm();
        form.setUsername(currentUser.getUsername());
        form.setAvatarUrl(currentUser.getAvatarUrl());
        model.addAttribute("form", form);
        return "profile";
    }

    @PostMapping
    public String updateProfile(@Valid @ModelAttribute("form") ProfileEditForm form,
                                BindingResult errors,
                                @AuthenticationPrincipal CustomeUserDetails currentUser,
                                Model model) {
        if (errors.hasErrors()) {
            populateModel(model, currentUser.getId(), "my");
            return "profile";
        }
        try {
            userService.update(currentUser.getId(), form.getUsername(), form.getAvatarUrl());
        } catch (IllegalUserArgumentException e) {
            errors.rejectValue("username", "username.taken", e.getUsernameState());
            populateModel(model, currentUser.getId(), "my");
            return "profile";
        }
        return "redirect:/profile";
    }

    private void populateModel(Model model, Long userId, String section) {
        model.addAttribute("user", userService.findById(userId));
        model.addAttribute("recipes", "favorites".equals(section)
                ? recipeService.findManyByUserFavorites(userId)
                : recipeService.findManyByUserId(userId));
        model.addAttribute("section", section);
    }
}
