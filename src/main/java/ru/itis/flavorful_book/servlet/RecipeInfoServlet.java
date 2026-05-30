package ru.itis.flavorful_book.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.flavorful_book.DTO.RecipeInfoDTO;
import ru.itis.flavorful_book.DTO.ReviewDTO;
import ru.itis.flavorful_book.entity.Category;
import ru.itis.flavorful_book.entity.IngredientRecipe;
import ru.itis.flavorful_book.entity.User;
import ru.itis.flavorful_book.service.CategoryService;
import ru.itis.flavorful_book.service.IngredientRecipeService;
import ru.itis.flavorful_book.service.RecipeService;
import ru.itis.flavorful_book.service.ReviewService;

import java.io.IOException;
import java.util.List;

@WebServlet("/recipe/info/*")
public class RecipeInfoServlet extends HttpServlet {
    private RecipeService recipeService;

    private CategoryService categoryService;

    private IngredientRecipeService ingredientRecipeService;

    private ReviewService reviewService;

    public void init(ServletConfig config) throws ServletException {
        recipeService = (RecipeService) config.getServletContext().getAttribute("recipeService");
        categoryService = (CategoryService) config.getServletContext().getAttribute("categoryService");
        ingredientRecipeService = (IngredientRecipeService) config.getServletContext().getAttribute("ingredientRecipeService");
        reviewService = (ReviewService) config.getServletContext().getAttribute("reviewService");
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id;
        String pathInfo = req.getPathInfo();
        try {
            id = Long.parseLong(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            resp.sendError(404);
            return;
        }

        RecipeInfoDTO recipe;
        try {
            recipe = recipeService.findByIdInfoDTO(id);
            recipeService.updateViews(id);
            req.setAttribute("recipe", recipe);
        } catch (IllegalArgumentException e) {
            resp.sendError(404);
            return;
        }

        List<Category> categories = categoryService.findAllByRecipeId(recipe.id());
        List<IngredientRecipe> ingredients = ingredientRecipeService.findAll(recipe.id());
        List<ReviewDTO> reviews = reviewService.findAllByRecipeId(recipe.id());

        req.setAttribute("categories", categories);
        req.setAttribute("ingredients", ingredients);
        req.setAttribute("reviews", reviews);

        try {
            User user = (User) req.getAttribute("user");
            ReviewDTO userReview = reviewService.findByUserIdRecipeId(user.getId(), id);
            req.setAttribute("userReview", userReview);
            req.setAttribute("isFavorite", recipeService.isInFavorites(user.getId(), id));
        } catch (RuntimeException e) {
            req.setAttribute("userReview", null);
            req.setAttribute("isFavorite", false);
        }

        req.getRequestDispatcher("/WEB-INF/views/recipe-info.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getAttribute("user");
        long id;
        String pathInfo = req.getPathInfo();
        try {
            id = Long.parseLong(pathInfo.substring(1));
            recipeService.addToFavorites(user.getId(), id);
        } catch (RuntimeException ignored) {
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getAttribute("user");
        long id;
        String pathInfo = req.getPathInfo();
        try {
            id = Long.parseLong(pathInfo.substring(1));
            recipeService.deleteFromFavorites(user.getId(), id);
        } catch (RuntimeException ignored) {
        }


    }
}
