package ru.coolz.fileshare.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.coolz.fileshare.service.AuthService;

import java.io.IOException;

public class AuthFilter implements Filter {

    private final AuthService authService;

    public AuthFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Cookie[] cookies = request.getCookies();
        String sessionId = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("SESSIONID")) {
                    sessionId = cookie.getValue();
                    break;
                }
            }
        }
        if (sessionId != null && authService.getUsernameBySession(sessionId) != null) {
            filterChain.doFilter(request, response);
        }else {
            response.sendRedirect(request.getContextPath() + "/login.html");
        }
    }
}
