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

        System.out.println("WorkSync: Dashboard request received.");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            System.out.println("WorkSync: No authenticated session.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        System.out.println("WorkSync: Dashboard session verified.");

        request.getRequestDispatcher(
                "/views/dashboard/dashboard.jsp"
        ).forward(request, response);
    }
}

