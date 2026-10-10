package com.worksync.dao;

import com.worksync.constants.DepartmentStatus;
import com.worksync.model.Department;
import com.worksync.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    // Get active departments for Employee Management
    public List<Department> findAll() throws SQLException {
        List<Department> departments = new ArrayList<>();

        String sql = "SELECT department_id, department_name, description, status " +
                "FROM departments " +
                "WHERE status = 'ACTIVE' " +
                "ORDER BY department_name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                departments.add(mapDepartment(rs));
            }
        }

        return departments;
    }

    // Get all departments, including inactive ones, with search
    public List<Department> findAllForManagement(String search)
            throws SQLException {

        List<Department> departments = new ArrayList<>();

        String sql = "SELECT department_id, department_name, description, status " +
                "FROM departments " +
                "WHERE department_name LIKE ? " +
                "ORDER BY department_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String keyword = search == null ? "" : search.trim();
            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    departments.add(mapDepartment(rs));
                }
            }
        }

        return departments;
    }

    // Find a department by ID
    public Department findById(int departmentId) throws SQLException {

        String sql = "SELECT department_id, department_name, description, status " +
                "FROM departments WHERE department_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, departmentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapDepartment(rs);
                }
            }
        }

        return null;
    }

    // Add a new department
    public boolean save(Department department) throws SQLException {

        String sql = "INSERT INTO departments " +
                "(department_name, description, status) " +
                "VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, department.getDepartmentName().trim());
            ps.setString(2, department.getDescription());

            DepartmentStatus status = department.getStatus() == null
                    ? DepartmentStatus.ACTIVE
                    : department.getStatus();

            ps.setString(3, status.name());

            return ps.executeUpdate() == 1;
        }
    }

    // Update department details
    public boolean update(Department department) throws SQLException {

        String sql = "UPDATE departments SET " +
                "department_name = ?, description = ? " +
                "WHERE department_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, department.getDepartmentName().trim());
            ps.setString(2, department.getDescription());
            ps.setInt(3, department.getDepartmentId());

            return ps.executeUpdate() == 1;
        }
    }

    // Activate or deactivate a department
    public boolean updateStatus(int departmentId, DepartmentStatus status)
            throws SQLException {

        String sql = "UPDATE departments SET status = ? " +
                "WHERE department_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setInt(2, departmentId);

            return ps.executeUpdate() == 1;
        }
    }

    // Convert a database row into a Department object
    private Department mapDepartment(ResultSet rs) throws SQLException {

        Department department = new Department();

        department.setDepartmentId(rs.getInt("department_id"));
        department.setDepartmentName(rs.getString("department_name"));
        department.setDescription(rs.getString("description"));

        String status = rs.getString("status");

        if (status != null) {
            department.setStatus(DepartmentStatus.valueOf(status));
        } else {
            department.setStatus(DepartmentStatus.ACTIVE);
        }

        return department;
    }


}
