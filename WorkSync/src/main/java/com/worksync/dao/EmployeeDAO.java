package com.worksync.dao;

import com.worksync.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.worksync.model.Employee;

public class EmployeeDAO {
    public List<Employee> findAll() throws SQLException {
        List<Employee> employees = new ArrayList<>();

        String sql = "SELECT e.employee_id, e.user_id, e.department_id, " +
                "e.manager_id, e.first_name, e.last_name, e.phone, " +
                "e.job_title, e.employment_status, e.joining_date, " +
                "d.department_name " +
                "FROM employees e " +
                "LEFT JOIN departments d ON e.department_id = d.department_id " +
                "ORDER BY e.employee_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Employee employee = new Employee();

                employee.setEmployeeId(resultSet.getInt("employee_id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setPhone(resultSet.getString("phone"));
                employee.setJobTitle(resultSet.getString("job_title"));

                String status = resultSet.getString("employment_status");

                if (status != null) {
                    employee.setEmploymentStatus(
                            com.worksync.constants.EmploymentStatus.valueOf(status)
                    );
                }

                java.sql.Date joiningDate = resultSet.getDate("joining_date");

                if (joiningDate != null) {
                    employee.setJoiningDate(joiningDate.toLocalDate());
                }

                employees.add(employee);
            }
        }

        return employees;
    }


}
