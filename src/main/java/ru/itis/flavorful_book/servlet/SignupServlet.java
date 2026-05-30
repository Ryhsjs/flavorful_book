package ru.itis.flavorful_book.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.flavorful_book.exception.IllegalUserArgumentException;
import ru.itis.flavorful_book.service.SecurityService;

import java.io.IOException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    private SecurityService securityService;

    @Override
    public void init(ServletConfig config)throws SecurityException {
        this.securityService = (SecurityService) config.getServletContext().getAttribute("securityService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String passwordRepeat = req.getParameter("passwordRepeat");
        String sessionId;
        try {
            sessionId = securityService.signupUser(username, email, password, passwordRepeat);
        } catch (IllegalUserArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("usernameState", e.getUsernameState());
            req.setAttribute("emailState", e.getEmailState());
            req.setAttribute("passwordState", e.getPasswordState());
            req.setAttribute("email", email);
            req.setAttribute("username", username);
            req.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(req, resp);
            return;
        }
        Cookie cookie = new Cookie("sessionId", sessionId);
        cookie.setMaxAge(60 * 60 * 30);
        resp.addCookie(cookie);
        resp.sendRedirect(req.getContextPath());
    }
}
