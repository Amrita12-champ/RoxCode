package com.worksync.dao;

import com.worksync.constants.EmploymentStatus;
import com.worksync.model.Department;
import com.worksync.model.Employee;
import com.worksync.model.User;
import com.worksync.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private static final String EMPLOYEE_SELECT =
            "SELECT e.employee_id, e.user_id, e.department_id, " +
                    "e.manager_id, e.first_name, e.last_name, e.phone, " +
                    "e.job_title, e.employment_status, e.joining_date, " +
                    "d.department_name, u.username, " +
                    "m.first_name AS manager_first_name, " +
                    "m.last_name AS manager_last_name " +
                    "FROM employees e " +
                    "LEFT JOIN departments d ON e.department_id = d.department_id " +
                    "LEFT JOIN users u ON e.user_id = u.user_id " +
                    "LEFT JOIN employees m ON e.manager_id = m.employee_id ";

    public List<Employee> findAll() throws SQLException {

        List<Employee> employees = new ArrayList<>();

        String sql = EMPLOYEE_SELECT +
                "ORDER BY e.employee_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                employees.add(mapEmployee(rs));
            }
        }

        return employees;
    }

    public Employee findById(int employeeId) throws SQLException {

        String sql = EMPLOYEE_SELECT +
                "WHERE e.employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, employeeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapEmployee(rs);
                }
            }
        }

        return null;
    }

    public boolean update(Employee employee) throws SQLException {

        String sql = "UPDATE employees SET " +
                "department_id = ?, " +
                "manager_id = ?, " +
                "first_name = ?, " +
                "last_name = ?, " +
                "phone = ?, " +
                "job_title = ?, " +
                "employment_status = ?, " +
                "joining_date = ? " +
                "WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            Integer departmentId = employee.getDepartment() != null
                    ? employee.getDepartment().getDepartmentId()
                    : null;

            Integer managerId = employee.getManager() != null
                    ? employee.getManager().getEmployeeId()
                    : null;

            if (departmentId != null) {
                ps.setInt(1, departmentId);
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }

            if (managerId != null) {
                ps.setInt(2, managerId);
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }

            ps.setString(3, employee.getFirstName());
            ps.setString(4, employee.getLastName());
            ps.setString(5, employee.getPhone());
            ps.setString(6, employee.getJobTitle());

            EmploymentStatus status = employee.getEmploymentStatus() != null
                    ? employee.getEmploymentStatus()
                    : EmploymentStatus.ACTIVE;

            ps.setString(7, status.name());

            if (employee.getJoiningDate() != null) {
                ps.setDate(
                        8,
                        java.sql.Date.valueOf(employee.getJoiningDate())
                );
            } else {
                ps.setNull(8, java.sql.Types.DATE);
            }

            ps.setInt(9, employee.getEmployeeId());

            return ps.executeUpdate() == 1;
        }
    }

    private Employee mapEmployee(ResultSet rs) throws SQLException {

        Employee employee = new Employee();

        employee.setEmployeeId(rs.getInt("employee_id"));
        employee.setFirstName(rs.getString("first_name"));
        employee.setLastName(rs.getString("last_name"));
        employee.setPhone(rs.getString("phone"));
        employee.setJobTitle(rs.getString("job_title"));

        String status = rs.getString("employment_status");

        if (status != null) {
            employee.setEmploymentStatus(
                    EmploymentStatus.valueOf(status)
            );
        }

        java.sql.Date joiningDate = rs.getDate("joining_date");

        if (joiningDate != null) {
            employee.setJoiningDate(joiningDate.toLocalDate());
        }

        int userId = rs.getInt("user_id");

        if (!rs.wasNull()) {
            User user = new User();
            user.setUserId(userId);
            user.setUsername(rs.getString("username"));
            employee.setUser(user);
        }

        int departmentId = rs.getInt("department_id");

        if (!rs.wasNull()) {
            Department department = new Department();
            department.setDepartmentId(departmentId);
            department.setDepartmentName(
                    rs.getString("department_name")
            );
            employee.setDepartment(department);
        }

        int managerId = rs.getInt("manager_id");

        if (!rs.wasNull()) {
            Employee manager = new Employee();

            manager.setEmployeeId(managerId);
            manager.setFirstName(
                    rs.getString("manager_first_name")
            );
            manager.setLastName(
                    rs.getString("manager_last_name")
            );

            employee.setManager(manager);
        }

        return employee;
    }
}