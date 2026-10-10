package com.worksync.controller;

import com.worksync.constants.DepartmentStatus;
import com.worksync.model.Department;
import com.worksync.service.DepartmentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/departments")
public class DepartmentServlet extends HttpServlet {

    private DepartmentService departmentService;

    @Override
    public void init() {
        departmentService = new DepartmentService();
    }

    private boolean isLoggedIn(HttpServletRequest request,
                               HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request, response)) {
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                request.setAttribute("formAction", "create");
                request.getRequestDispatcher(
                        "/views/departments/department-form.jsp"
                ).forward(request, response);
                return;
            }

            if ("edit".equals(action)) {
                showEditForm(request, response);
                return;
            }

            if ("toggleStatus".equals(action)) {
                toggleDepartmentStatus(request, response);
                return;
            }

            String search = request.getParameter("search");

            List<Department> departments =
                    departmentService.searchDepartments(search);

            request.setAttribute("departments", departments);
            request.setAttribute("search", search == null ? "" : search);

            request.getRequestDispatcher(
                    "/views/departments/departments.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/departments");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());

            try {
                request.setAttribute(
                        "departments",
                        departmentService.searchDepartments("")
                );

                request.setAttribute("search", "");

                request.getRequestDispatcher(
                        "/views/departments/departments.jsp"
                ).forward(request, response);

            } catch (SQLException sqlException) {
                throw new ServletException(
                        "Unable to load departments.",
                        sqlException
                );
            }
        } catch (SQLException e) {
            throw new ServletException("Unable to load department information.", e);
        }
    }

    private void showEditForm(HttpServletRequest request,
                              HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        String id = request.getParameter("id");

        if (id == null || id.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/departments");
            return;
        }

        int departmentId = Integer.parseInt(id);

        Department department =
                departmentService.getDepartmentById(departmentId);

        if (department == null) {
            response.sendRedirect(request.getContextPath() + "/departments");
            return;
        }

        request.setAttribute("department", department);
        request.setAttribute("formAction", "update");

        request.getRequestDispatcher(
                "/views/departments/department-form.jsp"
        ).forward(request, response);
    }

    private void toggleDepartmentStatus(HttpServletRequest request,
                                        HttpServletResponse response)
            throws IOException, SQLException {

        String id = request.getParameter("id");
        String status = request.getParameter("status");

        if (id == null || id.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/departments");
            return;
        }

        int departmentId = Integer.parseInt(id);
        DepartmentStatus departmentStatus =
                DepartmentStatus.valueOf(status);

        departmentService.updateDepartmentStatus(
                departmentId, departmentStatus.name()
        );

        response.sendRedirect(
                request.getContextPath() + "/departments?updated=true"
        );
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!isLoggedIn(request, response)) {
            return;
        }

        String action = request.getParameter("action");

        try {
            String departmentName = request.getParameter("departmentName");
            String description = request.getParameter("description");

            if (departmentName == null || departmentName.trim().isEmpty()) {
                request.setAttribute("error", "Department name is required.");

                if ("update".equals(action)) {
                    String id = request.getParameter("departmentId");

                    if (id != null && !id.trim().isEmpty()) {
                        Department department =
                                departmentService.getDepartmentById(
                                        Integer.parseInt(id)
                                );

                        request.setAttribute("department", department);
                    }
                }

                request.setAttribute("formAction", action);
                request.getRequestDispatcher(
                        "/views/departments/department-form.jsp"
                ).forward(request, response);
                return;
            }

            Department department = new Department();
            department.setDepartmentName(departmentName.trim());
            department.setDescription(
                    description == null || description.trim().isEmpty()
                            ? null : description.trim()
            );

            if ("create".equals(action)) {
                departmentService.addDepartment(department);

                response.sendRedirect(
                        request.getContextPath() + "/departments?added=true"
                );

            } else if ("update".equals(action)) {
                String id = request.getParameter("departmentId");

                if (id == null || id.trim().isEmpty()) {
                    response.sendRedirect(
                            request.getContextPath() + "/departments"
                    );
                    return;
                }

                department.setDepartmentId(Integer.parseInt(id));

                departmentService.updateDepartment(department);

                response.sendRedirect(
                        request.getContextPath() + "/departments?updated=true"
                );

            } else {
                response.sendRedirect(
                        request.getContextPath() + "/departments"
                );
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/departments");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("formAction", action);
            request.getRequestDispatcher(
                    "/views/departments/department-form.jsp"
            ).forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Unable to save department information.", e);
        }
    }


}
