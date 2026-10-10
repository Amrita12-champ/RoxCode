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


}
