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

        String employeeSql =
                "SELECT e.employee_id, e.first_name, e.last_name, " +
                        "e.phone, e.job_title, e.joining_date, " +
                        "e.employment_status, d.department_name, u.email " +
                        "FROM employees e " +
                        "JOIN users u ON e.user_id = u.user_id " +
                        "LEFT JOIN departments d " +
                        "ON e.department_id = d.department_id " +
                        "WHERE e.user_id = ?";

        try (Connection connection = DBConnection.getConnection()) {

            int employeeId;

            // 1. Retrieve employee profile.
            try (PreparedStatement statement =
                         connection.prepareStatement(employeeSql)) {

                statement.setInt(1, userId);

                try (ResultSet result = statement.executeQuery()) {

                    if (!result.next()) {
                        response.sendError(
                                HttpServletResponse.SC_NOT_FOUND,
                                "Employee profile was not found."
                        );
                        return;
                    }

                    employeeId = result.getInt("employee_id");

                    request.setAttribute("employeeId", employeeId);
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
            }

            // 2. Retrieve current-month attendance statistics.
            String attendanceSql =
                    "SELECT COUNT(*) AS total_records, " +
                            "COALESCE(SUM(CASE WHEN status = 'PRESENT' " +
                            "THEN 1 ELSE 0 END), 0) AS present_days, " +
                            "COALESCE(SUM(CASE WHEN status = 'ABSENT' " +
                            "THEN 1 ELSE 0 END), 0) AS absent_days, " +
                            "COALESCE(SUM(CASE WHEN status = 'HALF_DAY' " +
                            "THEN 1 ELSE 0 END), 0) AS half_days " +
                            "FROM attendance " +
                            "WHERE employee_id = ? " +
                            "AND attendance_date >= DATE_FORMAT(CURDATE(), '%Y-%m-01') " +
                            "AND attendance_date < " +
                            "DATE_ADD(DATE_FORMAT(CURDATE(), '%Y-%m-01'), INTERVAL 1 MONTH)";

            try (PreparedStatement statement =
                         connection.prepareStatement(attendanceSql)) {

                statement.setInt(1, employeeId);

                try (ResultSet result = statement.executeQuery()) {

                    if (result.next()) {
                        request.setAttribute("totalAttendanceRecords",
                                result.getInt("total_records"));

                        request.setAttribute("presentDays",
                                result.getInt("present_days"));

                        request.setAttribute("absentDays",
                                result.getInt("absent_days"));

                        request.setAttribute("halfDays",
                                result.getInt("half_days"));
                    }
                }
            }

            // 3. Retrieve leave application statistics.
            String leaveSql =
                    "SELECT COUNT(*) AS total_leaves, " +
                            "COALESCE(SUM(CASE WHEN status = 'PENDING' " +
                            "THEN 1 ELSE 0 END), 0) AS pending_leaves, " +
                            "COALESCE(SUM(CASE WHEN status = 'APPROVED' " +
                            "THEN 1 ELSE 0 END), 0) AS approved_leaves, " +
                            "COALESCE(SUM(CASE WHEN status = 'REJECTED' " +
                            "THEN 1 ELSE 0 END), 0) AS rejected_leaves " +
                            "FROM leave_requests " +
                            "WHERE employee_id = ?";

            try (PreparedStatement statement =
                         connection.prepareStatement(leaveSql)) {

                statement.setInt(1, employeeId);

                try (ResultSet result = statement.executeQuery()) {

                    if (result.next()) {
                        request.setAttribute("totalLeaves",
                                result.getInt("total_leaves"));

                        request.setAttribute("pendingLeaves",
                                result.getInt("pending_leaves"));

                        request.setAttribute("approvedLeaves",
                                result.getInt("approved_leaves"));

                        request.setAttribute("rejectedLeaves",
                                result.getInt("rejected_leaves"));
                    }
                }
            }

            // 4. Forward data to the employee dashboard.
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