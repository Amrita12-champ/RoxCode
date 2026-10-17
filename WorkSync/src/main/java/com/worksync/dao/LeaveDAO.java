package com.worksync.dao;

import com.worksync.model.LeaveRequest;
import com.worksync.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LeaveDAO {

    public List<LeaveRequest> getAllLeaveRequests()
            throws SQLException {

        List<LeaveRequest> requests = new ArrayList<>();

        String sql =
                "SELECT l.leave_id, l.employee_id, " +
                        "e.first_name, e.last_name, d.department_name, " +
                        "l.leave_type, l.start_date, l.end_date, " +
                        "l.reason, l.status, l.created_at, u.username " +
                        "FROM leave_requests l " +
                        "JOIN employees e ON l.employee_id = e.employee_id " +
                        "LEFT JOIN departments d ON e.department_id = d.department_id " +
                        "LEFT JOIN users u ON l.reviewed_by = u.user_id " +
                        "ORDER BY l.created_at DESC, l.leave_id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {

                LeaveRequest leave = new LeaveRequest();

                leave.setLeaveId(rs.getInt("leave_id"));
                leave.setEmployeeId(rs.getInt("employee_id"));
                leave.setFirstName(rs.getString("first_name"));
                leave.setLastName(rs.getString("last_name"));
                leave.setDepartmentName(rs.getString("department_name"));
                leave.setLeaveType(rs.getString("leave_type"));
                leave.setStartDate(rs.getDate("start_date"));
                leave.setEndDate(rs.getDate("end_date"));
                leave.setReason(rs.getString("reason"));
                leave.setStatus(rs.getString("status"));
                leave.setCreatedAt(rs.getTimestamp("created_at"));
                leave.setReviewerUsername(rs.getString("username"));

                requests.add(leave);
            }
        }

        return requests;
    }

    public void updateLeaveStatus(
            int leaveId,
            String status,
            int reviewedBy) throws SQLException {

        String sql =
                "UPDATE leave_requests " +
                        "SET status = ?, reviewed_by = ?, " +
                        "reviewed_at = CURRENT_TIMESTAMP " +
                        "WHERE leave_id = ? AND status = 'PENDING'";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setInt(2, reviewedBy);
            statement.setInt(3, leaveId);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new SQLException(
                        "Leave request was not found or is no longer pending.");
            }
        }
    }

    public void createLeaveRequest(
            int employeeId,
            String leaveType,
            Date startDate,
            Date endDate,
            String reason) throws SQLException {

        String sql =
                "INSERT INTO leave_requests " +
                        "(employee_id, leave_type, start_date, end_date, reason) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, employeeId);
            statement.setString(2, leaveType);
            statement.setDate(3, startDate);
            statement.setDate(4, endDate);
            statement.setString(5, reason);

            statement.executeUpdate();
        }
    }
}

