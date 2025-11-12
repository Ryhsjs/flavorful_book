package ru.itis.flavorful_book.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.flavorful_book.services.SecurityService;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private SecurityService securityService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.securityService = (SecurityService) config.getServletContext().getAttribute("securityService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("sessionId".equals(cookie.getName())) {
                    if (!securityService.logoutUser(cookie.getValue())) {
                        resp.sendError(500);
                        return;
                    }
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    resp.addCookie(cookie);
                }
            }
        }

        req.getSession().invalidate();
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
