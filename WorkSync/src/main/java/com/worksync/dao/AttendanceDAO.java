package com.worksync.dao;

import com.worksync.model.Attendance;
import com.worksync.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    // Get attendance records for a particular date
    public List<Attendance> getAttendanceByDate(Date date)
            throws SQLException {

        List<Attendance> attendanceList = new ArrayList<>();

        String sql =
                "SELECT a.attendance_id, a.employee_id, " +
                        "e.first_name, e.last_name, d.department_name, " +
                        "a.attendance_date, a.check_in, a.check_out, " +
                        "a.status, a.remarks " +
                        "FROM attendance a " +
                        "JOIN employees e ON a.employee_id = e.employee_id " +
                        "LEFT JOIN departments d ON e.department_id = d.department_id " +
                        "WHERE a.attendance_date = ? " +
                        "ORDER BY e.first_name, e.last_name";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setDate(1, date);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Attendance attendance = new Attendance();

                    attendance.setAttendanceId(
                            rs.getInt("attendance_id"));

                    attendance.setEmployeeId(
                            rs.getInt("employee_id"));

                    attendance.setFirstName(
                            rs.getString("first_name"));

                    attendance.setLastName(
                            rs.getString("last_name"));

                    attendance.setDepartmentName(
                            rs.getString("department_name"));

                    attendance.setAttendanceDate(
                            rs.getDate("attendance_date"));

                    attendance.setCheckIn(
                            rs.getTime("check_in"));

                    attendance.setCheckOut(
                            rs.getTime("check_out"));

                    attendance.setStatus(
                            rs.getString("status"));

                    attendance.setRemarks(
                            rs.getString("remarks"));

                    attendanceList.add(attendance);
                }
            }
        }

        return attendanceList;
    }


    // Save new attendance or update existing attendance
    public void saveAttendance(
            int employeeId,
            Date date,
            String status,
            String checkIn,
            String checkOut,
            String remarks,
            Integer markedBy) throws SQLException {

        String sql =
                "INSERT INTO attendance " +
                        "(employee_id, attendance_date, status, " +
                        "check_in, check_out, remarks, marked_by) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "status = VALUES(status), " +
                        "check_in = VALUES(check_in), " +
                        "check_out = VALUES(check_out), " +
                        "remarks = VALUES(remarks), " +
                        "marked_by = VALUES(marked_by)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, employeeId);
            statement.setDate(2, date);
            statement.setString(3, status);

            // Convert check-in time safely
            if (checkIn == null || checkIn.trim().isEmpty()) {

                statement.setNull(4, java.sql.Types.TIME);

            } else {

                statement.setTime(4, convertTime(checkIn));
            }

            // Convert check-out time safely
            if (checkOut == null || checkOut.trim().isEmpty()) {

                statement.setNull(5, java.sql.Types.TIME);

            } else {

                statement.setTime(5, convertTime(checkOut));
            }

            statement.setString(6, remarks);

            if (markedBy == null) {

                statement.setNull(7, java.sql.Types.INTEGER);

            } else {

                statement.setInt(7, markedBy);
            }

            statement.executeUpdate();

            System.out.println(
                    "WorkSync: Attendance saved successfully for employee ID "
                            + employeeId + " on " + date);
        }
    }


    // Convert HTML time input (HH:mm) into SQL time (HH:mm:ss)
    private java.sql.Time convertTime(String timeValue) {

        if (timeValue == null || timeValue.trim().isEmpty()) {
            return null;
        }

        timeValue = timeValue.trim();

        // HTML time input normally returns HH:mm
        if (timeValue.matches("\\d{2}:\\d{2}")) {
            timeValue = timeValue + ":00";
        }

        return java.sql.Time.valueOf(timeValue);
    }
}

