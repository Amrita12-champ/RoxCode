package com.worksync.controller;

import com.worksync.constants.EmploymentStatus;
import com.worksync.model.Department;
import com.worksync.model.Employee;
import com.worksync.service.DepartmentService;
import com.worksync.service.EmployeeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {

    private EmployeeService employeeService;
    private DepartmentService departmentService;

    @Override
    public void init() {
        employeeService = new EmployeeService();
        departmentService = new DepartmentService();
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

        String action = request.getParameter("action");

        try {

            if ("edit".equals(action)) {

                String id = request.getParameter("id");

                if (id == null || id.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/employees");
                    return;
                }

                int employeeId = Integer.parseInt(id);

                Employee employee = employeeService.getEmployeeById(employeeId);

                if (employee == null) {
                    response.sendRedirect(request.getContextPath() + "/employees");
                    return;
                }

                request.setAttribute("employee", employee);

                request.setAttribute(
                        "departments",
                        departmentService.getAllDepartments()
                );

                List<Employee> employees = employeeService.getAllEmployees();

                request.setAttribute("managers", employees);

                request.getRequestDispatcher(
                        "/views/employees/edit-employee.jsp"
                ).forward(request, response);

                return;
            }

            List<Employee> employees = employeeService.getAllEmployees();

            request.setAttribute("employees", employees);

            request.getRequestDispatcher(
                    "/views/employees/employees.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {

            response.sendRedirect(request.getContextPath() + "/employees");

        } catch (SQLException e) {

            throw new ServletException("Unable to process employee request.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        if (!"update".equals(action)) {
            response.sendRedirect(request.getContextPath() + "/employees");
            return;
        }

        try {

            String id = request.getParameter("employeeId");

            if (id == null || id.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/employees");
                return;
            }

            int employeeId = Integer.parseInt(id);

            Employee employee = employeeService.getEmployeeById(employeeId);

            if (employee == null) {
                response.sendRedirect(request.getContextPath() + "/employees");
                return;
            }

            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");

            if (firstName == null || firstName.trim().isEmpty()
                    || lastName == null || lastName.trim().isEmpty()) {

                request.setAttribute("error", "First name and last name are required.");
                request.setAttribute("employee", employee);
                request.setAttribute("departments", departmentService.getAllDepartments());
                request.setAttribute("managers", employeeService.getAllEmployees());

                request.getRequestDispatcher(
                        "/views/employees/edit-employee.jsp"
                ).forward(request, response);

                return;
            }

            employee.setFirstName(firstName.trim());
            employee.setLastName(lastName.trim());
            employee.setPhone(request.getParameter("phone"));
            employee.setJobTitle(request.getParameter("jobTitle"));

            String status = request.getParameter("employmentStatus");

            if (status != null && !status.trim().isEmpty()) {
                employee.setEmploymentStatus(
                        EmploymentStatus.valueOf(status)
                );
            }

            String departmentId = request.getParameter("departmentId");

            if (departmentId != null && !departmentId.trim().isEmpty()) {

                Department department = new Department();
                department.setDepartmentId(Integer.parseInt(departmentId));

                employee.setDepartment(department);

            } else {
                employee.setDepartment(null);
            }

            String managerId = request.getParameter("managerId");

            if (managerId != null && !managerId.trim().isEmpty()) {

                int selectedManagerId = Integer.parseInt(managerId);

                if (selectedManagerId == employeeId) {
                    request.setAttribute(
                            "error",
                            "An employee cannot be their own manager."
                    );

                    request.setAttribute("employee", employee);
                    request.setAttribute("departments", departmentService.getAllDepartments());
                    request.setAttribute("managers", employeeService.getAllEmployees());

                    request.getRequestDispatcher(
                            "/views/employees/edit-employee.jsp"
                    ).forward(request, response);

                    return;
                }

                Employee manager = new Employee();
                manager.setEmployeeId(selectedManagerId);

                employee.setManager(manager);

            } else {
                employee.setManager(null);
            }

            String joiningDate = request.getParameter("joiningDate");

            if (joiningDate != null && !joiningDate.trim().isEmpty()) {
                employee.setJoiningDate(LocalDate.parse(joiningDate));
            } else {
                employee.setJoiningDate(null);
            }

            employeeService.updateEmployee(employee);

            response.sendRedirect(
                    request.getContextPath() + "/employees?updated=true"
            );

        } catch (IllegalArgumentException e) {

            throw new ServletException("Invalid employee details.", e);

        } catch (SQLException e) {

            throw new ServletException("Unable to update employee.", e);
        }
    }
}