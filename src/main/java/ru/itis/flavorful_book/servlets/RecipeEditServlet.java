package ru.itis.flavorful_book.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.flavorful_book.DTO.RecipeSaveDTO;
import ru.itis.flavorful_book.exceptions.IllegalRecipeArgumentException;
import ru.itis.flavorful_book.models.Recipe;
import ru.itis.flavorful_book.models.User;
import ru.itis.flavorful_book.models.enums.Unit;
import ru.itis.flavorful_book.services.CategoryService;
import ru.itis.flavorful_book.services.IngredientRecipeService;
import ru.itis.flavorful_book.services.RecipeService;

import java.io.IOException;
import java.util.List;

@WebServlet("/recipe/edit/*")
public class RecipeEditServlet extends HttpServlet {
    private RecipeService recipeService;

    private CategoryService categoryService;

    private IngredientRecipeService ingredientRecipeService;

    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.recipeService = (RecipeService) config.getServletContext().getAttribute("recipeService");
        this.categoryService = (CategoryService) config.getServletContext().getAttribute("categoryService");
        this.ingredientRecipeService = (IngredientRecipeService) config.getServletContext().getAttribute("ingredientRecipeService");
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        List<Unit> units = List.of(Unit.values());

        if (pathInfo != null && !pathInfo.equals("/")) {
            long id;
            try {
                id = Long.parseLong(pathInfo.substring(1));
            } catch (NumberFormatException e) {
                resp.sendError(404);
                return;
            }
            RecipeSaveDTO recipe;
            try {
                recipe = recipeService.findByIdSaveDTO(id);
                req.setAttribute("recipe", recipe);
            } catch (IllegalArgumentException e) {
                resp.sendError(404);
                return;
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }

        req.setAttribute("units", units);

        req.getRequestDispatcher("/WEB-INF/views/recipe-edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RecipeSaveDTO recipeDto;
        try {
            recipeDto = objectMapper.readValue(
                    req.getParameter("recipeJson"),
                    RecipeSaveDTO.class
            );

        } catch (JsonProcessingException e) {
            resp.sendError(400);
            return;
        }

        try {
            Recipe recipe = recipeService.save(recipeDto);
            categoryService.saveAll(recipe.getId(), recipeDto.categories());
            ingredientRecipeService.saveAll(recipe.getId(), recipeDto.ingredients());
            resp.sendRedirect(req.getContextPath() + "/recipe/info/" + recipe.getId());
        } catch (IllegalRecipeArgumentException e) {
            req.setAttribute("error", e);
            req.setAttribute("recipe", recipeDto);
            List<Unit> units = List.of(Unit.values());
            req.setAttribute("units", units);
            req.getRequestDispatcher("/WEB-INF/views/recipe-edit.jsp").forward(req, resp);
        }
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = ((User) req.getAttribute("user")).getId();

        Long recipeId = extractId(req);
        if (recipeId == null) {
            resp.sendError(400);
            return;
        }
        RecipeSaveDTO recipe = recipeService.findByIdSaveDTO(recipeId);
        if (!userId.equals(recipe.userId())) {
            resp.sendError(403);
            return;
        }

        if (recipeService.deleteById(recipeId))
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

    }


    private Long extractId(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/"))
            return null;

        long id;
        try {
            id = Long.parseLong(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            return null;
        }

        return id;

    }
}

