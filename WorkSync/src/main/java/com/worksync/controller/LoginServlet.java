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

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("WorkSync: Login request received.");

        try {
            User user = userService.authenticate(username, password);

            System.out.println("WorkSync: Authentication result = "
                    + (user != null));

            if (user == null) {
                request.setAttribute("error",
                        "Invalid username or password, or inactive account.");

                request.getRequestDispatcher("/login.jsp")
                        .forward(request, response);
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

            System.out.println("WorkSync: Session created successfully.");

            response.sendRedirect(
                    request.getContextPath() + "/dashboard"
            );

        } catch (SQLException e) {
            System.err.println("WorkSync database error during login:");
            e.printStackTrace();

            throw new ServletException(
                    "Database error during login.", e
            );

        } catch (RuntimeException e) {
            System.err.println("WorkSync authentication error:");
            e.printStackTrace();

            throw new ServletException(
                    "Authentication processing failed.", e
            );
        }
    }
}

