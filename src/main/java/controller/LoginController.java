package controller;

import dao.StudentDb;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/LoginController")
public class LoginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get credentials from login.html form
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        StudentDb db = new StudentDb();

        // 2. Check Student table first
        String studentPass = db.search(email);

        // 3. Check Admin table if student search fails
        String adminPass = db.SearchAdm(email);

        // 4. Authenticate User
        if (studentPass != null && studentPass.equals(password)) {
            // Successful Student Login
            HttpSession session = request.getSession();
            session.setAttribute("userEmail", email);
            session.setAttribute("userRole", "STUDENT");

            response.sendRedirect("DashBoardController");

        } else if (adminPass != null && adminPass.equals(password)) {
            // Successful Admin Login
            HttpSession session = request.getSession();
            session.setAttribute("userEmail", email);
            session.setAttribute("userRole", "ADMIN");

            response.sendRedirect("DashBoardController");

        } else {
            // Invalid credentials response
            response.setContentType("text/html");
            response.getWriter().println("<h3 style='color:red; text-align:center;'>Invalid Email or Password!</h3>");
            response.getWriter().println("<div style='text-align:center;'><a href='login.html'>Try Again</a></div>");
        }
    }
}