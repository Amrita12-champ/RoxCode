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

public class LeaveApplicationDAO {

    public int findEmployeeIdByUserId(int userId) throws SQLException {

        String sql = "SELECT employee_id FROM employees WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("employee_id");
                }
            }
        }

        return -1;
    }

    public boolean applyForLeave(
            int employeeId,
            String leaveType,
            Date startDate,
            Date endDate,
            String reason) throws SQLException {

        String sql = "INSERT INTO leave_requests "
                + "(employee_id, leave_type, start_date, end_date, reason, status) "
                + "VALUES (?, ?, ?, ?, ?, 'PENDING')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            ps.setString(2, leaveType);
            ps.setDate(3, startDate);
            ps.setDate(4, endDate);
            ps.setString(5, reason);

            return ps.executeUpdate() == 1;
        }
    }

    public List<LeaveRequest> getMyLeaveRequests(int employeeId)
            throws SQLException {

        List<LeaveRequest> requests = new ArrayList<>();
        String sql = "SELECT leave_id, employee_id, leave_type, start_date, end_date, "
                + "reason, status, created_at "
                + "FROM leave_requests "
                + "WHERE employee_id = ? "
                + "ORDER BY created_at DESC, leave_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, employeeId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    LeaveRequest leave = new LeaveRequest();

                    leave.setLeaveId(rs.getInt("leave_id"));
                    leave.setEmployeeId(rs.getInt("employee_id"));
                    leave.setLeaveType(rs.getString("leave_type"));
                    leave.setStartDate(rs.getDate("start_date"));
                    leave.setEndDate(rs.getDate("end_date"));
                    leave.setReason(rs.getString("reason"));
                    leave.setStatus(rs.getString("status"));

                    requests.add(leave);
                }
            }
        }

        return requests;
    }

}
