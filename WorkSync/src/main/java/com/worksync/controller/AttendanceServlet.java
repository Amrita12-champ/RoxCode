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
import java.util.List;

@WebServlet("/attendance")
public class AttendanceServlet extends HttpServlet {

    private AttendanceDAO attendanceDAO;

    @Override
    public void init() {
        attendanceDAO = new AttendanceDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("userId") == null) {

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String selectedDate = request.getParameter("date");

        if (selectedDate == null || selectedDate.isEmpty()) {
            selectedDate = LocalDate.now().toString();
        }

        try {
            Date date = Date.valueOf(selectedDate);

            List<Attendance> attendanceList =
                    attendanceDAO.getAttendanceByDate(date);

            request.setAttribute("attendanceList", attendanceList);
            request.setAttribute("selectedDate", selectedDate);

            request.getRequestDispatcher(
                    "/views/dashboard/attendance.jsp"
            ).forward(request, response);

        } catch (IllegalArgumentException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid attendance date."
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load attendance records.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("userId") == null) {

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int employeeId = Integer.parseInt(
                    request.getParameter("employeeId"));

            Date date = Date.valueOf(
                    request.getParameter("attendanceDate"));

            String status = request.getParameter("status");
            String checkIn = request.getParameter("checkIn");
            String checkOut = request.getParameter("checkOut");
            String remarks = request.getParameter("remarks");

            if (!"PRESENT".equals(status)
                    && !"ABSENT".equals(status)
                    && !"HALF_DAY".equals(status)
                    && !"ON_LEAVE".equals(status)) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid attendance status."
                );
                return;
            }

            if (("PRESENT".equals(status)
                    || "HALF_DAY".equals(status))
                    && (checkIn == null || checkIn.isEmpty())) {

                request.getSession().setAttribute(
                        "attendanceError",
                        "Check-in time is required for present or half-day attendance."
                );

                response.sendRedirect(
                        request.getContextPath() +
                                "/attendance?date=" + date
                );
                return;
            }

            Integer markedBy = (Integer) session.getAttribute("userId");

            attendanceDAO.saveAttendance(
                    employeeId,
                    date,
                    status,
                    checkIn,
                    checkOut,
                    remarks,
                    markedBy
            );

            response.sendRedirect(
                    request.getContextPath() +
                            "/attendance?date=" + date
            );

        } catch (IllegalArgumentException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid employee ID, date, or time."
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to save attendance.", e);
        }
    }
}