package com.worksync.controller;

import com.worksync.util.DBConnection;
import com.worksync.util.HibernateUtil;
import com.worksync.util.PasswordUtil;
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

@WebServlet("/reset-password")
public class PasswordResetServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        try {
            SessionFactory sessionFactory =
                    HibernateUtil.getSessionFactory();

            // Initialize Hibernate and mapped models.
            sessionFactory.getMetamodel();
        } catch (Exception e) {
            throw new ServletException(
                    "Unable to initialize Hibernate.", e
            );
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

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword =
                request.getParameter("confirmPassword");

        if (username == null || username.trim().isEmpty()
                || newPassword == null
                || newPassword.length() < 8
                || confirmPassword == null) {

            showError(
                    request,
                    response,
                    "Enter your username and a password of at least 8 characters."
            );
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showError(
                    request,
                    response,
                    "The new password and confirmation do not match."
            );
            return;
        }

        String checkSql =
                "SELECT user_id FROM users " +
                        "WHERE username = ? AND role = 'EMPLOYEE' " +
                        "AND status = 'ACTIVE'";

        String updateSql =
                "UPDATE users SET password = ? " +
                        "WHERE username = ? AND role = 'EMPLOYEE' " +
                        "AND status = 'ACTIVE'";

        try (Connection connection = DBConnection.getConnection()) {

            int userId;

            try (PreparedStatement checkStatement =
                         connection.prepareStatement(checkSql)) {

                checkStatement.setString(1, username.trim());

                try (ResultSet resultSet =
                             checkStatement.executeQuery()) {

                    if (!resultSet.next()) {
                        showError(
                                request,
                                response,
                                "Active employee account not found."
                        );
                        return;
                    }

                    userId = resultSet.getInt("user_id");
                }
            }

            String hashedPassword =
                    PasswordUtil.hashPassword(newPassword);

            try (PreparedStatement updateStatement =
                         connection.prepareStatement(updateSql)) {

                updateStatement.setString(1, hashedPassword);
                updateStatement.setString(2, username.trim());

                int rowsUpdated = updateStatement.executeUpdate();

                if (rowsUpdated == 1) {
                    response.sendRedirect(
                            request.getContextPath()
                                    + "/login?passwordReset=success"
                    );
                    return;
                }

                showError(
                        request,
                        response,
                        "Password could not be updated. Please try again."
                );
            }

        } catch (SQLException e) {
            throw new ServletException(
                    "Database error while resetting password.", e
            );
        }
    }

    private void showError(HttpServletRequest request,
                           HttpServletResponse response,
                           String message)
            throws ServletException, IOException {

        request.setAttribute("error", message);

        request.getRequestDispatcher(
                "/views/reset-password.jsp"
        ).forward(request, response);
    }


}
