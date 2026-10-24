package com.worksync.controller;

import com.worksync.util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.worksync.util.DBConnection;

@WebServlet("/reset-password")
public class PasswordResetServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        try {
            SessionFactory sessionFactory =
                    HibernateUtil.getSessionFactory();

            // Initialize Hibernate so the mapped table can be created.
            sessionFactory.getMetamodel();
        } catch (Exception e) {
            throw new ServletException(
                    "Unable to initialize Hibernate", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/views/reset-password.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword =
                request.getParameter("confirmPassword");

        if (username == null || username.trim().isEmpty()
                || newPassword == null || newPassword.length() < 8
                || confirmPassword == null) {

            request.setAttribute("error",
                    "Enter your username and a password of at least 8 characters.");
            doGet(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error",
                    "Passwords do not match.");
            doGet(request, response);
            return;
        }

        String sql =
                "UPDATE users SET password = ? WHERE username = ? AND role = 'EMPLOYEE'";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, newPassword);
            statement.setString(2, username.trim());

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                request.setAttribute("success",
                        "Password updated successfully. Please log in.");
            } else {
                request.setAttribute("error",
                        "Employee username was not found.");
            }

            doGetWithMessages(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to update password", e);
        }
    }

    private void doGetWithMessages(HttpServletRequest request,
                                   HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/views/reset-password.jsp"
        ).forward(request, response);
    }


}
