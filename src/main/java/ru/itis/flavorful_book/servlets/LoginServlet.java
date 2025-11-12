package ru.itis.flavorful_book.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.flavorful_book.exceptions.AuthenticationException;
import ru.itis.flavorful_book.services.SecurityService;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private SecurityService securityService;

    @Override
    public void init(ServletConfig config)throws SecurityException {
        this.securityService = (SecurityService) config.getServletContext().getAttribute("securityService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String sessionId;
        try {
            sessionId = securityService.loginUser(email, password);
        } catch (AuthenticationException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        } catch (RuntimeException e) {
            resp.sendError(500);
            return;
        }
        Cookie cookie = new Cookie("sessionId", sessionId);
        cookie.setMaxAge(60 * 60 * 30);
        resp.addCookie(cookie);
        resp.sendRedirect(req.getContextPath());
    }
}
