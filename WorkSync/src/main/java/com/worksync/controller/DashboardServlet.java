package com.worksync.controller;

import com.worksync.service.DashboardService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private DashboardService dashboardService;

    @Override
    public void init() throws ServletException {
        dashboardService = new DashboardService();
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

        String role = (String) session.getAttribute("role");

        if (!"SUPER_ADMIN".equals(role)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "You do not have permission to access this page."
            );
            return;
        }

        try {
            request.setAttribute(
                    "totalEmployees",
                    dashboardService.getTotalEmployees()
            );

            request.setAttribute(
                    "presentToday",
                    dashboardService.getPresentToday()
            );

            request.setAttribute(
                    "pendingLeaveRequests",
                    dashboardService.getPendingLeaveRequests()
            );

            request.setAttribute(
                    "totalDepartments",
                    dashboardService.getTotalDepartments()
            );

            System.out.println("WorkSync: Super Admin dashboard accessed.");

            request.getRequestDispatcher(
                    "/views/dashboard/dashboard.jsp"
            ).forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to retrieve dashboard statistics from the database.",
                    e
            );
        }
    }
}