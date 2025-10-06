package ru.coolz.fileshare.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.coolz.fileshare.service.AuthService;

import java.io.IOException;

@WebServlet
public class AuthServlet extends HttpServlet {

    private final AuthService authService;

    public AuthServlet(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        var username = req.getParameter("username");
        var password = req.getParameter("password");
        try{
            if (authService.authenticate(username, password)) {
                String sessionId = authService.createSession(username);
                Cookie cookie = new Cookie("SESSIONID", sessionId);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                resp.addCookie(cookie);

                resp.sendRedirect("/");
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("Invalid credentials");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
