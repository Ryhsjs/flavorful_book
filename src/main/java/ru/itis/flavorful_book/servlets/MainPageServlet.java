package ru.itis.flavorful_book.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.flavorful_book.DTO.RecipePreviewDTO;
import ru.itis.flavorful_book.services.RecipeService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("")
public class MainPageServlet extends HttpServlet {
    private RecipeService recipeService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.recipeService = (RecipeService) config.getServletContext().getAttribute("recipeService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer activeCookingTimeStart = null;
        Integer activeCookingTimeEnd = null;
        Integer totalCookingTimeStart = null;
        Integer totalCookingTimeEnd = null;

        try {
            activeCookingTimeStart = Integer.valueOf(req.getParameter("activeCookingTimeStart"));
        } catch (NumberFormatException ignored) {}

        try {
            activeCookingTimeEnd = Integer.valueOf(req.getParameter("activeCookingTimeEnd"));
        } catch (NumberFormatException ignored) {}

        try {
            totalCookingTimeStart = Integer.valueOf(req.getParameter("totalCookingTimeStart"));
        } catch (NumberFormatException ignored) {}

        try {
            totalCookingTimeEnd = Integer.valueOf(req.getParameter("totalCookingTimeEnd"));
        } catch (NumberFormatException ignored) {}



        String[] ingredientParams = req.getParameterValues("ingredientId");
        String[] categoryParams = req.getParameterValues("categoryId");
        boolean isStrictMatchIngredients = Boolean.parseBoolean(req.getParameter("isStrictMatchIngredients"));
        boolean isStrictMatchCategories = Boolean.parseBoolean(req.getParameter("isStrictMatchCategories"));
        List<Long> ingredientIds = new ArrayList<>();
        List<Long> categoryIds = new ArrayList<>();

        if (ingredientParams != null) {
            for (String param : ingredientParams) {
                try {
                    ingredientIds.add(Long.valueOf(param));
                } catch (NumberFormatException ignored) {
                }
            }
        }

        if (categoryParams != null) {
             for (String param : categoryParams) {
                try {
                    categoryIds.add(Long.valueOf(param));
                } catch (NumberFormatException ignored) {
                }
            }
        }

        List<RecipePreviewDTO> recipes = recipeService.findAll(ingredientIds, isStrictMatchIngredients,
                categoryIds, isStrictMatchCategories, activeCookingTimeStart, activeCookingTimeEnd,
                totalCookingTimeStart, totalCookingTimeEnd);

        req.setAttribute("recipes", recipes);

        req.getRequestDispatcher("/WEB-INF/views/main-page.jsp").forward(req, resp);
    }
}
