package ru.itis.flavorful_book.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import ru.itis.flavorful_book.service.ImageService;

import java.io.IOException;
import java.io.PrintWriter;

@MultipartConfig(
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024,
        fileSizeThreshold = 1024 * 1024
)
@WebServlet("/image/*")
public class ImageUploadServlet extends HttpServlet {
    private ImageService imageService;

    @Override
    public void init(ServletConfig config) throws SecurityException {
        this.imageService = (ImageService) config.getServletContext().getAttribute("imageService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        Part image = req.getPart("image");

        try {
            String imageUrl = null;
            if (pathInfo.equals("/recipe")) {
                imageUrl = imageService.saveRecipeImage(image);
            } else if (pathInfo.equals("/avatar")) {
                imageUrl = imageService.saveUserAvatar(image);
            }

            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.print(imageUrl);
        } catch (IllegalArgumentException e) {
            req.setAttribute("errorImage", e.getMessage());
        }
    }
}
