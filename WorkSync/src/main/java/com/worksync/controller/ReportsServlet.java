package com.worksync.controller;

import com.worksync.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/reports")
public class ReportsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = String.valueOf(session.getAttribute("role"));

        if (!"SUPER_ADMIN".equals(role)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "You are not authorized to access Reports."
            );
            return;
        }

        LocalDate today = LocalDate.now();

        LocalDate startDate = parseDate(
                request.getParameter("startDate"),
                today.withDayOfMonth(1)
        );

        LocalDate endDate = parseDate(
                request.getParameter("endDate"),
                today
        );

        if (startDate.isAfter(endDate)) {
            LocalDate temp = startDate;
            startDate = endDate;
            endDate = temp;
        }

        request.setAttribute("startDate", startDate.toString());
        request.setAttribute("endDate", endDate.toString());

        if ("csv".equalsIgnoreCase(request.getParameter("export"))) {
            exportAttendanceCsv(
                    response,
                    startDate,
                    endDate
            );
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {

            // Workforce statistics
            String workforceSql =
                    "SELECT COUNT(*) AS total_employees, " +
                            "COALESCE(SUM(CASE WHEN employment_status = 'ACTIVE' " +
                            "THEN 1 ELSE 0 END), 0) AS active_employees " +
                            "FROM employees";

            try (PreparedStatement ps =
                         connection.prepareStatement(workforceSql);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    request.setAttribute(
                            "totalEmployees",
                            rs.getInt("total_employees")
                    );

                    request.setAttribute(
                            "activeEmployees",
                            rs.getInt("active_employees")
                    );
                }
            }

            // Department statistics
            String departmentSql =
                    "SELECT COUNT(*) FROM departments";

            try (PreparedStatement ps =
                         connection.prepareStatement(departmentSql);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    request.setAttribute(
                            "totalDepartments",
                            rs.getInt(1)
                    );
                }
            }

            // Attendance statistics for the selected date range
            String attendanceSql =
                    "SELECT " +
                            "COUNT(*) AS total_records, " +
                            "COALESCE(SUM(CASE WHEN status = 'PRESENT' " +
                            "THEN 1 ELSE 0 END), 0) AS present_count, " +
                            "COALESCE(SUM(CASE WHEN status = 'ABSENT' " +
                            "THEN 1 ELSE 0 END), 0) AS absent_count, " +
                            "COALESCE(SUM(CASE WHEN status = 'HALF_DAY' " +
                            "THEN 1 ELSE 0 END), 0) AS half_day_count, " +
                            "COALESCE(SUM(CASE WHEN status = 'ON_LEAVE' " +
                            "THEN 1 ELSE 0 END), 0) AS on_leave_count " +
                            "FROM attendance " +
                            "WHERE attendance_date BETWEEN ? AND ?";

            try (PreparedStatement ps =
                         connection.prepareStatement(attendanceSql)) {

                ps.setDate(1, Date.valueOf(startDate));
                ps.setDate(2, Date.valueOf(endDate));

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        request.setAttribute(
                                "totalAttendance",
                                rs.getInt("total_records")
                        );

                        request.setAttribute(
                                "presentCount",
                                rs.getInt("present_count")
                        );

                        request.setAttribute(
                                "absentCount",
                                rs.getInt("absent_count")
                        );

                        request.setAttribute(
                                "halfDayCount",
                                rs.getInt("half_day_count")
                        );

                        request.setAttribute(
                                "onLeaveCount",
                                rs.getInt("on_leave_count")
                        );
                    }
                }
            }

            // Leave statistics for the selected date range
            String leaveSql =
                    "SELECT " +
                            "COUNT(*) AS total_requests, " +
                            "COALESCE(SUM(CASE WHEN status = 'PENDING' " +
                            "THEN 1 ELSE 0 END), 0) AS pending_count, " +
                            "COALESCE(SUM(CASE WHEN status = 'APPROVED' " +
                            "THEN 1 ELSE 0 END), 0) AS approved_count, " +
                            "COALESCE(SUM(CASE WHEN status = 'REJECTED' " +
                            "THEN 1 ELSE 0 END), 0) AS rejected_count " +
                            "FROM leave_requests " +
                            "WHERE created_at >= ? " +
                            "AND created_at < DATE_ADD(?, INTERVAL 1 DAY)";

            try (PreparedStatement ps =
                         connection.prepareStatement(leaveSql)) {

                ps.setDate(1, Date.valueOf(startDate));
                ps.setDate(2, Date.valueOf(endDate));

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        request.setAttribute(
                                "totalLeaveRequests",
                                rs.getInt("total_requests")
                        );

                        request.setAttribute(
                                "pendingLeaves",
                                rs.getInt("pending_count")
                        );

                        request.setAttribute(
                                "approvedLeaves",
                                rs.getInt("approved_count")
                        );

                        request.setAttribute(
                                "rejectedLeaves",
                                rs.getInt("rejected_count")
                        );
                    }
                }
            }

            // Employee count by department
            String departmentReportSql =
                    "SELECT d.department_name, " +
                            "COUNT(e.employee_id) AS employee_count " +
                            "FROM departments d " +
                            "LEFT JOIN employees e " +
                            "ON d.department_id = e.department_id " +
                            "GROUP BY d.department_id, d.department_name " +
                            "ORDER BY d.department_name";

            try (PreparedStatement ps =
                         connection.prepareStatement(departmentReportSql);
                 ResultSet rs = ps.executeQuery()) {

                request.setAttribute(
                        "departmentReport",
                        rs
                );

                /*
                 * Do not forward a JDBC ResultSet to a JSP after
                 * closing its connection. Copy the rows first.
                 */
                java.util.List<java.util.Map<String, Object>> departments =
                        new java.util.ArrayList<>();

                while (rs.next()) {
                    java.util.Map<String, Object> row =
                            new java.util.HashMap<>();

                    row.put(
                            "departmentName",
                            rs.getString("department_name")
                    );

                    row.put(
                            "employeeCount",
                            rs.getInt("employee_count")
                    );

                    departments.add(row);
                }

                request.setAttribute(
                        "departmentReport",
                        departments
                );
            }

            request.getRequestDispatcher(
                    "/views/reports/reports.jsp"
            ).forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to generate reports.", e
            );
        }
    }

    private LocalDate parseDate(String value, LocalDate defaultValue) {

        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }

        try {
            return LocalDate.parse(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private void exportAttendanceCsv(HttpServletResponse response,
                                     LocalDate startDate,
                                     LocalDate endDate)
            throws IOException, ServletException {

        String sql =
                "SELECT a.attendance_date, " +
                        "e.employee_id, e.first_name, e.last_name, " +
                        "d.department_name, a.check_in, a.check_out, a.status " +
                        "FROM attendance a " +
                        "JOIN employees e ON a.employee_id = e.employee_id " +
                        "LEFT JOIN departments d " +
                        "ON e.department_id = d.department_id " +
                        "WHERE a.attendance_date BETWEEN ? AND ? " +
                        "ORDER BY a.attendance_date DESC, e.employee_id";

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=\"worksync-attendance-report.csv\""
        );

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             PrintWriter writer = response.getWriter()) {

            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));

            writer.println(
                    "Attendance Date,Employee ID,First Name,Last Name," +
                            "Department,Check In,Check Out,Status"
            );

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    writer.println(
                            csv(rs.getString("attendance_date")) + "," +
                                    csv(rs.getString("employee_id")) + "," +
                                    csv(rs.getString("first_name")) + "," +
                                    csv(rs.getString("last_name")) + "," +
                                    csv(rs.getString("department_name")) + "," +
                                    csv(rs.getString("check_in")) + "," +
                                    csv(rs.getString("check_out")) + "," +
                                    csv(rs.getString("status"))
                    );
                }
            }

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to export attendance report.", e
            );
        }
    }

    private String csv(String value) {
        if (value == null) {
            return "";
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}