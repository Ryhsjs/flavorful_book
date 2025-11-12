package ru.itis.flavorful_book.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.flavorful_book.DTO.RecipePreviewDTO;
import ru.itis.flavorful_book.models.User;
import ru.itis.flavorful_book.services.RecipeService;
import ru.itis.flavorful_book.services.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private UserService userService;

    private RecipeService recipeService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.userService = (UserService) config.getServletContext().getAttribute("userService");
        this.recipeService = (RecipeService) config.getServletContext().getAttribute("recipeService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getAttribute("user");
        String section = req.getParameter("section");

        List<RecipePreviewDTO> recipes;

        if (section != null && section.equals("favorites")) {
            recipes = recipeService.findManyByUserFavorites(user.getId());
            req.setAttribute("section", "favorites");
        } else {
            recipes = recipeService.findManyByUserId(user.getId());
            req.setAttribute("section", "my-recipes");
        }

        req.setAttribute("recipes", recipes);

        req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String avatarUrl =  req.getParameter("avatarUrl");

        User user = (User) req.getAttribute("user");

        userService.update(user.getId(), username, avatarUrl);

        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}
