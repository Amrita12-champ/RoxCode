package controller;
import dao.AdminDb;
import dao.StudentDb;
import entity.Admin;
import entity.Student;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegistrationController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Matches name="fullName", name="email", name="password", name="role" from register.jsp
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        System.out.println("Processing Registration -> Role: " + role + ", Name: " + fullName + ", Email: " + email);

        int result = 0;

        if ("INSTRUCTOR".equalsIgnoreCase(role)) {
            // Insert into admin_register table
            Admin admin = new Admin();
            admin.setName(fullName);
            admin.setEmail(email);
            admin.setPassword(password);

            AdminDb adminDb = new AdminDb();
            adminDb.connection();
            result = adminDb.insert(admin);
            System.out.println("Admin insertion status: " + result);

        } else {
            // Insert into register (student) table
            Student student = new Student();
            student.setName(fullName);
            student.setEmail(email);
            student.setPassword(password);

            StudentDb studentDb = new StudentDb();
            studentDb.connection();
            result = studentDb.insert(student);
            System.out.println("Student insertion status: " + result);
        }
        if (result > 0) {
            response.sendRedirect("login.jsp");
        } else {
            response.getWriter().println("Registration failed! Please try again.");
        }
    }
}