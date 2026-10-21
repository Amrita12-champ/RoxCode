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

        System.out.println(
                "Mapped URL: /apply-leave"
        );
    }

    // Open the leave application page
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check whether the user is logged in
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        // Correct JSP path: views/employees/
        String jspPath =
                "/views/employees/leave-application.jsp";

        // Verify that the JSP exists
        if (getServletContext().getResource(jspPath) == null) {
            throw new ServletException(
                    "Leave application JSP not found: " + jspPath
            );
        }

        // Forward to the leave application page
        request.getRequestDispatcher(jspPath)
                .forward(request, response);
    }

    // Process leave application form submission
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check whether the user is logged in
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        request.setCharacterEncoding("UTF-8");

        try {
            int userId = Integer.parseInt(
                    session.getAttribute("userId").toString()
            );

            String leaveType = request.getParameter("leaveType");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String reason = request.getParameter("reason");

            // Validate required fields
            if (leaveType == null || leaveType.isEmpty()
                    || startDate == null || startDate.isEmpty()
                    || endDate == null || endDate.isEmpty()
                    || reason == null || reason.isEmpty()) {

                request.setAttribute(
                        "errorMessage",
                        "Please fill in all required fields."
                );

                request.getRequestDispatcher(
                        "/views/employees/leave-application.jsp"
                ).forward(request, response);

                return;
            }

            // Convert date strings
            Date fromDate = Date.valueOf(startDate);
            Date toDate = Date.valueOf(endDate);

            // Validate date range
            if (toDate.before(fromDate)) {
                request.setAttribute(
                        "errorMessage",
                        "End date cannot be before the start date."
                );

                request.getRequestDispatcher(
                        "/views/employees/leave-application.jsp"
                ).forward(request, response);

                return;
            }

            /*
             * SUBMISSION SECTION
             *
             * Connect this section to your existing LeaveRequest
             * constructor and LeaveApplicationDAO submission method.
             *
             * I have not guessed those method signatures because
             * they must match your existing project.
             */

            request.setAttribute(
                    "leaveType", leaveType
            );

            request.setAttribute(
                    "startDate", startDate
            );

            request.setAttribute(
                    "endDate", endDate
            );

            request.setAttribute(
                    "reason", reason
            );

            request.setAttribute(
                    "errorMessage",
                    "Form validation passed, but the database submission method must be connected."
            );

            request.getRequestDispatcher(
                    "/views/employees/leave-application.jsp"
            ).forward(request, response);

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "errorMessage",
                    "Invalid date or form data. Please check your entries."
            );

            request.getRequestDispatcher(
                    "/views/employees/leave-application.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            throw new ServletException(
                    "Error processing leave application.",
                    e
            );
        }
    }
}

