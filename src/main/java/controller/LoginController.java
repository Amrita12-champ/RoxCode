package controller;

import dao.AdminDb;
import dao.StudentDb;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email != null) {
            email = email.trim();
        }

        // 1. Check in Student Table (register)
        StudentDb sdb = new StudentDb();
        sdb.connection();
        boolean isStudent = sdb.validateStudent(email, password);

        // 2. Check in Admin Table (Admin_register)
        AdminDb adb = new AdminDb();
        adb.connection();
        boolean isAdmin = adb.validateAdmin(email, password);

        if (isStudent || isAdmin) {
            // Save logged-in user in session
            HttpSession session = req.getSession();
            session.setAttribute("loggedUser", email);
            session.setAttribute("userRole", isAdmin ? "ADMIN" : "STUDENT");

            System.out.println("Login successful for: " + email + " as " + (isAdmin ? "ADMIN" : "STUDENT"));

            // Redirect to dashboard
            resp.sendRedirect("dashboard.jsp");
        } else {
            System.out.println("Login failed for: " + email);
            resp.setContentType("text/html");
            resp.getWriter().println("<script>alert('Invalid Email or Password'); window.location.href='login.jsp';</script>");
        }
    }
}