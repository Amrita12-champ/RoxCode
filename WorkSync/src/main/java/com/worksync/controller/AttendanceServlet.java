package com.worksync.controller;

import com.worksync.dao.AttendanceDAO;
import com.worksync.model.Attendance;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet("/attendance")
public class AttendanceServlet extends HttpServlet {

    private AttendanceDAO attendanceDAO;

    @Override
    public void init() throws ServletException {
        attendanceDAO = new AttendanceDAO();
        System.out.println("WorkSync: AttendanceServlet initialized.");
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String selectedDate = request.getParameter("date");

        if (selectedDate == null || selectedDate.isEmpty()) {
            selectedDate = LocalDate.now().toString();
        }

        try {
            LocalDate parsedDate = LocalDate.parse(selectedDate);
            Date sqlDate = Date.valueOf(parsedDate);

            List<Attendance> attendanceList =
                    attendanceDAO.getAttendanceByDate(sqlDate);

            request.setAttribute("attendanceList", attendanceList);
            request.setAttribute("selectedDate", selectedDate);

            request.getRequestDispatcher(
                    "/views/dashboard/attendance.jsp"
            ).forward(request, response);

        } catch (DateTimeParseException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid date. Please select a valid date."
            );

        } catch (SQLException e) {

            log("WorkSync: Failed to load attendance records.", e);

            throw new ServletException(
                    "Unable to load attendance records from the database.", e
            );
        }
    }


    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("\n========== ATTENDANCE DEBUG ==========");
        System.out.println("POST request received.");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            System.out.println("ERROR: User session is missing.");

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String employeeIdValue = request.getParameter("employeeId");
        String dateValue = request.getParameter("attendanceDate");
        String status = request.getParameter("status");
        String checkIn = request.getParameter("checkIn");
        String checkOut = request.getParameter("checkOut");
        String remarks = request.getParameter("remarks");

        System.out.println("Employee ID = " + employeeIdValue);
        System.out.println("Attendance Date = " + dateValue);
        System.out.println("Status = " + status);
        System.out.println("Check-in = " + checkIn);
        System.out.println("Check-out = " + checkOut);
        System.out.println("Remarks = " + remarks);

        try {
            if (employeeIdValue == null || employeeIdValue.isEmpty()) {
                throw new IllegalArgumentException("Employee ID is missing.");
            }

            if (dateValue == null || dateValue.isEmpty()) {
                throw new IllegalArgumentException("Attendance date is missing.");
            }

            if (status == null || status.isEmpty()) {
                throw new IllegalArgumentException("Attendance status is missing.");
            }

            int employeeId = Integer.parseInt(employeeIdValue);
            Date date = Date.valueOf(dateValue);

            if (!"PRESENT".equals(status)
                    && !"ABSENT".equals(status)
                    && !"HALF_DAY".equals(status)
                    && !"ON_LEAVE".equals(status)) {
                throw new IllegalArgumentException(
                        "Invalid attendance status: " + status);
            }

            if (checkIn != null && !checkIn.isEmpty()) {
                java.sql.Time.valueOf(checkIn + (checkIn.length() == 5 ? ":00" : ""));
            }

            if (checkOut != null && !checkOut.isEmpty()) {
                java.sql.Time.valueOf(checkOut + (checkOut.length() == 5 ? ":00" : ""));
            }

            if (("PRESENT".equals(status) || "HALF_DAY".equals(status))
                    && (checkIn == null || checkIn.isEmpty())) {
                throw new IllegalArgumentException(
                        "Check-in time is required for PRESENT or HALF_DAY.");
            }

            Integer markedBy = (Integer) session.getAttribute("userId");

            System.out.println("Validation passed. Saving attendance...");

            attendanceDAO.saveAttendance(
                    employeeId,
                    date,
                    status,
                    checkIn,
                    checkOut,
                    remarks,
                    markedBy
            );

            System.out.println("SUCCESS: Attendance saved to database.");
            System.out.println("======================================\n");

            response.sendRedirect(
                    request.getContextPath() + "/attendance?date=" + date
            );

        } catch (IllegalArgumentException | NullPointerException e) {

            System.out.println("VALIDATION ERROR: " + e.getMessage());
            e.printStackTrace();

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Attendance validation failed: " + e.getMessage()
            );

        } catch (SQLException e) {

            System.out.println("DATABASE ERROR: " + e.getMessage());
            e.printStackTrace();

            throw new ServletException("Unable to save attendance.", e);
        }
    }


}