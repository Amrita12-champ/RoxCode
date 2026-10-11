package com.worksync.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Step 1: Check whether the user is logged in
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Step 2: Allow only Super Admin
        String role = (String) session.getAttribute("role");

        if (!"SUPER_ADMIN".equals(role)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "You do not have permission to access this page."
            );
            return;
        }

        // Step 3: Forward to the Super Admin dashboard
        System.out.println("WorkSync: Super Admin dashboard accessed.");

        request.getRequestDispatcher(
                "/views/dashboard/dashboard.jsp"
        ).forward(request, response);
    }
}