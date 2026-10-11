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

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        System.out.println("WorkSync LoginServlet initialized.");
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = userService.authenticate(username, password);

            if (user == null) {
                request.setAttribute(
                        "error",
                        "Invalid username or password, or inactive account."
                );

                request.getRequestDispatcher("/login.jsp")
                        .forward(request, response);
                return;
            }

            String role = user.getRole().name();

            HttpSession oldSession = request.getSession(false);

            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);

            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", role);

            switch (role) {
                case "SUPER_ADMIN":
                    response.sendRedirect(
                            request.getContextPath() + "/dashboard"
                    );
                    break;

                case "EMPLOYEE":
                    response.sendRedirect(
                            request.getContextPath() + "/employee-dashboard"
                    );
                    break;

                case "HR_MANAGER":
                    response.sendRedirect(
                            request.getContextPath() + "/hr-dashboard"
                    );
                    break;

                case "DEPARTMENT_MANAGER":
                    response.sendRedirect(
                            request.getContextPath() + "/manager-dashboard"
                    );
                    break;

                case "AUDITOR":
                    response.sendRedirect(
                            request.getContextPath() + "/auditor-dashboard"
                    );
                    break;

                default:
                    session.invalidate();

                    request.setAttribute(
                            "error",
                            "Your account has an unrecognized role. Please contact the administrator."
                    );

                    request.getRequestDispatcher("/login.jsp")
                            .forward(request, response);
            }

        } catch (SQLException e) {
            throw new ServletException(
                    "Database error during login.", e
            );
        }
    }
}

