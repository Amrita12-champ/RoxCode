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

@WebServlet("/apply-leave")
public class LeaveApplicationServlet extends HttpServlet {

    private LeaveApplicationDAO leaveDAO;

    @Override
    public void init() {
        leaveDAO = new LeaveApplicationDAO();
    }

    private boolean isEmployee(HttpSession session) {

        return session != null
                && session.getAttribute("userId") != null
                && "EMPLOYEE".equals(session.getAttribute("role"));
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (!isEmployee(session)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Only employees can access this page.");
            return;
        }

        try {

            int userId = ((Number) session.getAttribute("userId")).intValue();

            int employeeId = leaveDAO.findEmployeeIdByUserId(userId);

            if (employeeId == -1) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Your login account is not linked to an employee record.");
                return;
            }

            List<LeaveRequest> requests =
                    leaveDAO.getMyLeaveRequests(employeeId);

            request.setAttribute("leaveRequests", requests);

            request.getRequestDispatcher(
                            "/views/employee/leave-application.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Unable to load leave applications.", e);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (!isEmployee(session)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Only employees can submit leave applications.");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        try {

            int userId = ((Number) session.getAttribute("userId")).intValue();

            int employeeId = leaveDAO.findEmployeeIdByUserId(userId);

            if (employeeId == -1) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Your login account is not linked to an employee record.");
                return;
            }

            String leaveType = request.getParameter("leaveType");
            String startDateValue = request.getParameter("startDate");
            String endDateValue = request.getParameter("endDate");
            String reason = request.getParameter("reason");

            if (leaveType == null
                    || !(leaveType.equals("CASUAL")
                    || leaveType.equals("SICK")
                    || leaveType.equals("EARNED")
                    || leaveType.equals("UNPAID"))) {

                request.getSession().setAttribute(
                        "leaveMessage", "Please select a valid leave type.");

                response.sendRedirect(request.getContextPath() + "/apply-leave");
                return;
            }

            if (startDateValue == null || endDateValue == null
                    || startDateValue.isEmpty()
                    || endDateValue.isEmpty()) {

                request.getSession().setAttribute(
                        "leaveMessage", "Please select both leave dates.");

                response.sendRedirect(request.getContextPath() + "/apply-leave");
                return;
            }

            LocalDate start = LocalDate.parse(startDateValue);
            LocalDate end = LocalDate.parse(endDateValue);

            if (start.isBefore(LocalDate.now())) {
                request.getSession().setAttribute(
                        "leaveMessage", "Leave start date cannot be in the past.");

                response.sendRedirect(request.getContextPath() + "/apply-leave");
                return;
            }

            if (end.isBefore(start)) {
                request.getSession().setAttribute(
                        "leaveMessage", "End date cannot be earlier than start date.");

                response.sendRedirect(request.getContextPath() + "/apply-leave");
                return;
            }

            if (reason != null && reason.length() > 500) {
                request.getSession().setAttribute(
                        "leaveMessage", "Reason cannot exceed 500 characters.");

                response.sendRedirect(request.getContextPath() + "/apply-leave");
                return;
            }

            boolean saved = leaveDAO.applyForLeave(
                    employeeId,
                    leaveType,
                    Date.valueOf(start),
                    Date.valueOf(end),
                    reason == null || reason.isEmpty() ? null : reason.trim()
            );

            request.getSession().setAttribute(
                    "leaveMessage",
                    saved
                            ? "Leave application submitted successfully!"
                            : "Unable to submit your leave application."
            );

            response.sendRedirect(request.getContextPath() + "/apply-leave");

        } catch (IllegalArgumentException e) {

            request.getSession().setAttribute(
                    "leaveMessage", "Please enter valid leave dates.");

            response.sendRedirect(request.getContextPath() + "/apply-leave");

        } catch (SQLException e) {

            throw new ServletException("Unable to save leave application.", e);
        }
    }


}
