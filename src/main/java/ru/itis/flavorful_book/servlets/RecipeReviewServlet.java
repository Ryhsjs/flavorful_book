package ru.itis.flavorful_book.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.flavorful_book.models.User;
import ru.itis.flavorful_book.services.ReviewService;

import java.io.IOException;

@WebServlet("/recipe/review")
public class RecipeReviewServlet extends HttpServlet {
    private ReviewService reviewService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        reviewService = (ReviewService) config.getServletContext().getAttribute("reviewService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String comment = req.getParameter("comment");

        int rating;
        Long recipeId;
        try {
            rating = Integer.parseInt(req.getParameter("rating"));
            recipeId = Long.parseLong(req.getParameter("recipeId"));
        } catch (NumberFormatException e) {
            resp.sendError(400);
            return;
        }

        User user = (User) req.getAttribute("user");

        try {
            reviewService.save(user.getId(), recipeId, rating, comment);
        } catch (IllegalArgumentException e) {
            resp.sendError(400);
        }

        resp.sendRedirect(req.getContextPath() + "/recipe/info/" + recipeId);
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long reviewId;
        try {
            reviewId = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.sendError(400);
            return;
        }

        User user = (User) req.getAttribute("user");

        Long userId = reviewService.findById(reviewId).userId();

        if (userId.equals(user.getId())) {
            reviewService.deleteById(reviewId);
        } else {
            resp.sendError(403);
        }
    }
}
