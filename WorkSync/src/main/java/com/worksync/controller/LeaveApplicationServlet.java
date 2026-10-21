package com.worksync.controller;

import com.worksync.dao.LeaveApplicationDAO;
import com.worksync.model.LeaveRequest;

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

@WebServlet(
        name = "LeaveApplicationServlet",
        urlPatterns = {"/apply-leave"}
)
public class LeaveApplicationServlet extends HttpServlet {

    private LeaveApplicationDAO leaveApplicationDAO;

    @Override
    public void init() throws ServletException {
        leaveApplicationDAO = new LeaveApplicationDAO();

        System.out.println(
                "LeaveApplicationServlet initialized successfully!"
        );
        System.out.println("Mapped URL: /apply-leave");
    }

    private boolean isEmployee(HttpSession session) {
        return session != null
                && session.getAttribute("userId") != null
                && "EMPLOYEE".equals(session.getAttribute("role"));
    }

    // Display the employee's leave application page
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        if (!isEmployee(session)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only employees can access this page."
            );
            return;
        }

        try {
            int userId = Integer.parseInt(
                    session.getAttribute("userId").toString()
            );

            int employeeId =
                    leaveApplicationDAO.findEmployeeIdByUserId(userId);

            if (employeeId == -1) {
                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Your account is not linked to an employee record."
                );
                return;
            }

            List<LeaveRequest> leaveRequests =
                    leaveApplicationDAO.getMyLeaveRequests(employeeId);

            request.setAttribute("leaveRequests", leaveRequests);

            request.getRequestDispatcher(
                    "/views/employees/leave-application.jsp"
            ).forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to load leave applications.", e
            );
        }
    }

    // Save a new leave application
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("========== APPLY LEAVE POST ==========");

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        if (!isEmployee(session)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only employees can submit leave applications."
            );
            return;
        }

        try {
            int userId = Integer.parseInt(
                    session.getAttribute("userId").toString()
            );

            int employeeId =
                    leaveApplicationDAO.findEmployeeIdByUserId(userId);

            if (employeeId == -1) {
                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Your account is not linked to an employee record."
                );
                return;
            }

            String leaveType = request.getParameter("leaveType");
            String startDateValue = request.getParameter("startDate");
            String endDateValue = request.getParameter("endDate");
            String reason = request.getParameter("reason");

            System.out.println("User ID: " + userId);
            System.out.println("Employee ID: " + employeeId);
            System.out.println("Leave Type: " + leaveType);
            System.out.println("Start Date: " + startDateValue);
            System.out.println("End Date: " + endDateValue);

            // Validate leave type
            if (leaveType == null
                    || !(leaveType.equals("CASUAL")
                    || leaveType.equals("SICK")
                    || leaveType.equals("EARNED")
                    || leaveType.equals("UNPAID"))) {

                session.setAttribute(
                        "leaveMessage",
                        "Please select a valid leave type."
                );

                response.sendRedirect(
                        request.getContextPath() + "/apply-leave"
                );
                return;
            }

            // Validate dates
            if (startDateValue == null
                    || startDateValue.isEmpty()
                    || endDateValue == null
                    || endDateValue.isEmpty()) {

                session.setAttribute(
                        "leaveMessage",
                        "Please select both leave dates."
                );

                response.sendRedirect(
                        request.getContextPath() + "/apply-leave"
                );
                return;
            }

            LocalDate start = LocalDate.parse(startDateValue);
            LocalDate end = LocalDate.parse(endDateValue);

            if (start.isBefore(LocalDate.now())) {
                session.setAttribute(
                        "leaveMessage",
                        "Leave start date cannot be in the past."
                );

                response.sendRedirect(
                        request.getContextPath() + "/apply-leave"
                );
                return;
            }

            if (end.isBefore(start)) {
                session.setAttribute(
                        "leaveMessage",
                        "End date cannot be earlier than start date."
                );

                response.sendRedirect(
                        request.getContextPath() + "/apply-leave"
                );
                return;
            }

            if (reason != null && reason.trim().length() > 500) {
                session.setAttribute(
                        "leaveMessage",
                        "Reason cannot exceed 500 characters."
                );

                response.sendRedirect(
                        request.getContextPath() + "/apply-leave"
                );
                return;
            }

            // ACTUAL DATABASE INSERT
            boolean saved = leaveApplicationDAO.applyForLeave(
                    employeeId,
                    leaveType,
                    Date.valueOf(start),
                    Date.valueOf(end),
                    reason == null || reason.trim().isEmpty()
                            ? null
                            : reason.trim()
            );

            System.out.println("Leave saved to database: " + saved);

            session.setAttribute(
                    "leaveMessage",
                    saved
                            ? "Leave application submitted successfully!"
                            : "Unable to submit your leave application."
            );

            response.sendRedirect(
                    request.getContextPath() + "/apply-leave"
            );

        } catch (IllegalArgumentException e) {
            session.setAttribute(
                    "leaveMessage",
                    "Invalid form data or date. Please check your entries."
            );

            response.sendRedirect(
                    request.getContextPath() + "/apply-leave"
            );

        } catch (SQLException e) {
            throw new ServletException(
                    "Database error while submitting leave application.", e
            );
        }
    }
}

