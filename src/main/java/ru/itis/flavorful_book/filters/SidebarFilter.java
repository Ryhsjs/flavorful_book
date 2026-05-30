package ru.itis.flavorful_book.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.flavorful_book.entity.Category;
import ru.itis.flavorful_book.entity.Ingredient;
import ru.itis.flavorful_book.service.CategoryService;
import ru.itis.flavorful_book.service.IngredientService;

import java.io.IOException;
import java.util.List;

@WebFilter(urlPatterns = "*")
public class SidebarFilter extends HttpFilter {
    private final List<String> paths = List.of("/signup", "/login");

    private IngredientService ingredientService;

    private CategoryService categoryService;

    @Override
    public void init(FilterConfig config) throws ServletException {
        ingredientService = (IngredientService) config.getServletContext().getAttribute("ingredientService");
        categoryService = (CategoryService) config.getServletContext().getAttribute("categoryService");
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        if (paths.contains(req.getServletPath())) {
            chain.doFilter(req, res);
            return;
        }

        List<Ingredient> ingredients = ingredientService.findAll();
        List<Category> categories = categoryService.findAll();

        req.setAttribute("ingredients", ingredients);
        req.setAttribute("categories", categories);
        chain.doFilter(req, res);
    }
}
