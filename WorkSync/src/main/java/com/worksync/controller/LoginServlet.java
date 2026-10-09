package com.worksync.controller;

import com.worksync.model.User;
import com.worksync.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = userService.authenticate(username, password);

            if (user == null) {
                request.setAttribute("error", "Invalid username or password.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            HttpSession oldSession = request.getSession(false);

            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole().name());

            switch (user.getRole()) {
                case SUPER_ADMIN, HR_MANAGER ->
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                case DEPARTMENT_MANAGER ->
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                case EMPLOYEE ->
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                case AUDITOR ->
                        response.sendRedirect(request.getContextPath() + "/dashboard");
            }

        } catch (SQLException e) {
            throw new ServletException("Unable to authenticate user.", e);
        }
    }
}

