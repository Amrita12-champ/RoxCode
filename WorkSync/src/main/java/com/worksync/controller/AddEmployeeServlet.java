package com.worksync.controller;
import com.worksync.constants.AccountStatus;
import com.worksync.constants.EmploymentStatus;
import com.worksync.constants.Role;
import com.worksync.util.PasswordUtil;

import java.sql.*;
import java.time.LocalDate;

import com.worksync.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/employees/add")
public class AddEmployeeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String departmentSql =
                "SELECT department_id, department_name " +
                        "FROM departments WHERE status = 'ACTIVE' " +
                        "ORDER BY department_name";

        String managerSql =
                "SELECT employee_id, first_name, last_name " +
                        "FROM employees WHERE employment_status = 'ACTIVE' " +
                        "ORDER BY first_name, last_name";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement departmentStatement =
                     connection.prepareStatement(departmentSql);
             ResultSet departmentResults =
                     departmentStatement.executeQuery()) {

            request.setAttribute("departments", departmentResultsToList(departmentResults));

            try (PreparedStatement managerStatement =
                         connection.prepareStatement(managerSql);
                 ResultSet managerResults = managerStatement.executeQuery()) {

                request.setAttribute("managers", managerResultsToList(managerResults));
            }

            request.getRequestDispatcher("/views/employees/add-employee.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Unable to load employee form.", e);
        }
    }

    private java.util.List<java.util.Map<String, Object>> departmentResultsToList(
            ResultSet results) throws java.sql.SQLException {

        java.util.List<java.util.Map<String, Object>> list =
                new java.util.ArrayList<>();

        while (results.next()) {
            java.util.Map<String, Object> row = new java.util.HashMap<>();
            row.put("id", results.getInt("department_id"));
            row.put("name", results.getString("department_name"));
            list.add(row);
        }

        return list;
    }

    private java.util.List<java.util.Map<String, Object>> managerResultsToList(
            ResultSet results) throws java.sql.SQLException {

        java.util.List<java.util.Map<String, Object>> list =
                new java.util.ArrayList<>();

        while (results.next()) {
            java.util.Map<String, Object> row = new java.util.HashMap<>();
            row.put("id", results.getInt("employee_id"));
            row.put("name", results.getString("first_name") + " " +
                    results.getString("last_name"));
            list.add(row);
        }

        return list;
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String jobTitle = request.getParameter("jobTitle");
        String roleValue = request.getParameter("role");
        String statusValue = request.getParameter("status");
        String departmentValue = request.getParameter("departmentId");
        String managerValue = request.getParameter("managerId");
        String joiningDateValue = request.getParameter("joiningDate");

        if (username == null || username.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || password == null || password.length() < 8
                || firstName == null || firstName.trim().isEmpty()
                || lastName == null || lastName.trim().isEmpty()
                || jobTitle == null || jobTitle.trim().isEmpty()
                || joiningDateValue == null || joiningDateValue.trim().isEmpty()) {

            request.setAttribute("error",
                    "Please complete all required fields. Password must contain at least 8 characters.");
            doGet(request, response);
            return;
        }

        Connection connection = null;

        try {
            Role role = Role.valueOf(roleValue);
            EmploymentStatus employmentStatus =
                    EmploymentStatus.valueOf(statusValue);

            Integer departmentId = departmentValue == null
                    || departmentValue.trim().isEmpty()
                    ? null : Integer.valueOf(departmentValue);

            Integer managerId = managerValue == null
                    || managerValue.trim().isEmpty()
                    ? null : Integer.valueOf(managerValue);

            LocalDate joiningDate = LocalDate.parse(joiningDateValue);

            if (role == Role.SUPER_ADMIN) {
                throw new IllegalArgumentException(
                        "A Super Admin account cannot be created from this form.");
            }

            String hashedPassword = PasswordUtil.hashPassword(password);

            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            int userId;

            String userSql =
                    "INSERT INTO users (username, email, password, role, status) " +
                            "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(
                    userSql, Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, username.trim());
                statement.setString(2, email.trim());
                statement.setString(3, hashedPassword);
                statement.setString(4, role.name());

                AccountStatus accountStatus =
                        employmentStatus == EmploymentStatus.ACTIVE
                                ? AccountStatus.ACTIVE
                                : AccountStatus.INACTIVE;

                statement.setString(5, accountStatus.name());
                statement.executeUpdate();

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("Unable to create user account.");
                    }
                    userId = keys.getInt(1);
                }
            }

            String employeeSql =
                    "INSERT INTO employees " +
                            "(user_id, department_id, manager_id, first_name, last_name, " +
                            "phone, job_title, employment_status, joining_date) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement =
                         connection.prepareStatement(employeeSql)) {

                statement.setInt(1, userId);

                if (departmentId == null) {
                    statement.setNull(2, java.sql.Types.INTEGER);
                } else {
                    statement.setInt(2, departmentId);
                }

                if (managerId == null) {
                    statement.setNull(3, java.sql.Types.INTEGER);
                } else {
                    statement.setInt(3, managerId);
                }

                statement.setString(4, firstName.trim());
                statement.setString(5, lastName.trim());
                statement.setString(6,
                        phone == null || phone.trim().isEmpty()
                                ? null : phone.trim());
                statement.setString(7, jobTitle.trim());
                statement.setString(8, employmentStatus.name());
                statement.setDate(9, Date.valueOf(joiningDate));

                statement.executeUpdate();
            }

            connection.commit();

            response.sendRedirect(request.getContextPath() + "/employees?created=1");

        } catch (IllegalArgumentException e) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
            }

            request.setAttribute("error",
                    "Please check the selected role, status, date, or other field values.");
            doGet(request, response);

        } catch (SQLException e) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
            }

            if (e instanceof java.sql.SQLIntegrityConstraintViolationException) {
                request.setAttribute("error",
                        "That username or email may already exist, or the selected department or manager is invalid.");
                doGet(request, response);
            } else {
                throw new ServletException("Unable to create employee.", e);
            }

        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    getServletContext().log(
                            "Unable to close employee creation connection.", e);
                }
            }
        }


    }



}
