package com.worksync.service;

import com.worksync.constants.DepartmentStatus;
import com.worksync.dao.DepartmentDAO;
import com.worksync.model.Department;

import java.sql.SQLException;
import java.util.List;

public class DepartmentService {

    private final DepartmentDAO departmentDAO = new DepartmentDAO();

    public List<Department> getAllDepartments() throws SQLException {
        return departmentDAO.findAll();
    }

    public List<Department> searchDepartments(String search)
            throws SQLException {
        return departmentDAO.findAllForManagement(search);
    }

    public Department getDepartmentById(int departmentId)
            throws SQLException {

        if (departmentId <= 0) {
            throw new IllegalArgumentException("Invalid department ID.");
        }

        return departmentDAO.findById(departmentId);
    }

    public boolean addDepartment(Department department)
            throws SQLException {

        validateDepartment(department);
        return departmentDAO.save(department);
    }

    public boolean updateDepartment(Department department)
            throws SQLException {

        if (department == null || department.getDepartmentId() == null
                || department.getDepartmentId() <= 0) {
            throw new IllegalArgumentException("Invalid department details.");
        }

        validateDepartment(department);
        return departmentDAO.update(department);
    }

    public boolean updateDepartmentStatus(int departmentId, String status)
            throws SQLException {

        if (departmentId <= 0) {
            throw new IllegalArgumentException("Invalid department ID.");
        }

        DepartmentStatus departmentStatus;

        try {
            departmentStatus = DepartmentStatus.valueOf(status);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid department status.");
        }

        return departmentDAO.updateStatus(departmentId, departmentStatus);
    }

    private void validateDepartment(Department department) {

        if (department == null) {
            throw new IllegalArgumentException("Department details are required.");
        }

        if (department.getDepartmentName() == null
                || department.getDepartmentName().trim().isEmpty()) {
            throw new IllegalArgumentException("Department name is required.");
        }
    }


}
