package com.worksync.controller;

import com.worksync.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/employee-dashboard")
public class EmployeeDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");

        if (!"EMPLOYEE".equals(role)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "You do not have permission to access this dashboard."
            );
            return;
        }

        int userId = ((Number) session.getAttribute("userId")).intValue();

        String sql =
                "SELECT e.employee_id, e.first_name, e.last_name, " +
                        "e.phone, e.job_title, e.joining_date, " +
                        "e.employment_status, d.department_name, u.email " +
                        "FROM employees e " +
                        "JOIN users u ON e.user_id = u.user_id " +
                        "LEFT JOIN departments d ON e.department_id = d.department_id " +
                        "WHERE e.user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet result = statement.executeQuery()) {

                if (!result.next()) {
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Employee profile was not found."
                    );
                    return;
                }

                request.setAttribute("employeeId",
                        result.getInt("employee_id"));

                request.setAttribute("firstName",
                        result.getString("first_name"));

                request.setAttribute("lastName",
                        result.getString("last_name"));

                request.setAttribute("phone",
                        result.getString("phone"));

                request.setAttribute("jobTitle",
                        result.getString("job_title"));

                request.setAttribute("joiningDate",
                        result.getDate("joining_date"));

                request.setAttribute("employmentStatus",
                        result.getString("employment_status"));

                request.setAttribute("departmentName",
                        result.getString("department_name"));

                request.setAttribute("email",
                        result.getString("email"));
            }

            request.getRequestDispatcher(
                    "/views/dashboard/employee-dashboard.jsp"
            ).forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to load employee dashboard.", e
            );
        }
    }
}

