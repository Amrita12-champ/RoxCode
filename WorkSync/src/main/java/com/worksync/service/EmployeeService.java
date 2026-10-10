package com.worksync.service;

import com.worksync.dao.EmployeeDAO;
import com.worksync.model.Employee;

import java.sql.SQLException;
import java.util.List;

public class EmployeeService {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    public List<Employee> getAllEmployees() throws SQLException {
        return employeeDAO.findAll();
    }

    public Employee getEmployeeById(int employeeId) throws SQLException {
        if (employeeId <= 0) {
            throw new IllegalArgumentException("Invalid employee ID");
        }
        return employeeDAO.findById(employeeId);
    }

    public boolean updateEmployee(Employee employee) throws SQLException {
        if (employee == null || employee.getEmployeeId() <= 0) {
            throw new IllegalArgumentException("Invalid employee details");
        }
        if (employee.getFirstName() == null ||
                employee.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (employee.getLastName() == null ||
                employee.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        return employeeDAO.update(employee);
    }


}
