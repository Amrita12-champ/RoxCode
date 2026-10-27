package com.worksync.controller;

import com.worksync.dao.LeaveDAO;
import com.worksync.model.LeaveRequest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/leave-management")
public class LeaveServlet extends HttpServlet {

    private LeaveDAO leaveDAO;

    @Override
    public void init() {
        leaveDAO = new LeaveDAO();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("userId") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        if (!"SUPER_ADMIN".equals(
                session.getAttribute("role"))) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only Super Admin can access Leave Management.");
            return;
        }

        try {
            List<LeaveRequest> requests =
                    leaveDAO.getAllLeaveRequests();

            request.setAttribute("leaveRequests", requests);

            request.getRequestDispatcher(
                    "/views/dashboard/leave-management.jsp"
            ).forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to load leave requests.", e);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("userId") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        if (!"SUPER_ADMIN".equals(
                session.getAttribute("role"))) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only Super Admin can review leave requests.");
            return;
        }

        try {
            int leaveId = Integer.parseInt(
                    request.getParameter("leaveId"));

            String status = request.getParameter("status");

            if (!"APPROVED".equals(status)
                    && !"REJECTED".equals(status)) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid leave status.");
                return;
            }

            int reviewedBy =
                    ((Number) session.getAttribute("userId")).intValue();

            leaveDAO.updateLeaveStatus(
                    leaveId, status, reviewedBy);

            response.sendRedirect(
                    request.getContextPath() + "/leave-management");

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid leave request ID.");

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to update leave request.", e);
        }
    }
}

