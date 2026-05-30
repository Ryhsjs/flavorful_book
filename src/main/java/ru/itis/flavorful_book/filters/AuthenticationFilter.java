package ru.itis.flavorful_book.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.flavorful_book.exception.AuthenticationException;
import ru.itis.flavorful_book.entity.User;
import ru.itis.flavorful_book.service.SecurityService;

import java.io.IOException;
import java.util.List;

@WebFilter(urlPatterns = "*")
public class AuthenticationFilter extends HttpFilter {
    private final List<String> protectedPaths = List.of("/profile", "/profile/edit", "/recipe/edit", "/recipe/review");

    private SecurityService securityService;

    @Override
    public void init(FilterConfig config) throws ServletException {
        this.securityService = (SecurityService) config.getServletContext().getAttribute("securityService");
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        String sessionId = extractSessionId(req.getCookies());

        if (sessionId == null) {
            if (protectedPaths.contains(req.getServletPath()))
                res.sendRedirect(req.getContextPath() + "/login");
            else
                chain.doFilter(req, res);
            return;
        }

        try {
            User user = securityService.getUser(sessionId);
            req.setAttribute("user", user);

        } catch (AuthenticationException e) {
            if (protectedPaths.contains(req.getServletPath())) {
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }
        }
        chain.doFilter(req, res);
    }

    private String extractSessionId(Cookie[] cookies) {
        if (cookies == null)
            return null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("sessionId")) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
