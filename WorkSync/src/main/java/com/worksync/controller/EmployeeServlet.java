package com.worksync.controller;

import com.worksync.model.Employee;
import com.worksync.service.EmployeeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {

    private EmployeeService employeeService;

    @Override
    public void init() {
        employeeService = new EmployeeService();
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

        try {
            List<Employee> employees = employeeService.getAllEmployees();

            request.setAttribute("employees", employees);

            request.getRequestDispatcher("/views/employees/employees.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Unable to load employee records.", e);
        }
    }


}
