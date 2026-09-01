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

@WebServlet("/reset-password")
public class ResetPasswordController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        System.out.println(">>> 1. ResetPasswordController doPost TRIGGERED! <<<");

        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("email");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        System.out.println(">>> 2. Email from Session: [" + email + "]");
        System.out.println(">>> 3. New Password: [" + newPassword + "]");

        if (email == null) {
            resp.getWriter().println("Session expired! Please restart forgot password.");
            return;
        }

        if (newPassword == null || !newPassword.equals(confirmPassword)) {
            resp.getWriter().println("Passwords do not match!");
            return;
        }

        StudentDb sdb = new StudentDb();
        sdb.connection();
        boolean updated = sdb.updatePassword(email, newPassword);

        if (!updated) {
            AdminDb adb = new AdminDb();
            adb.connection();
            updated = adb.updatePassword(email, newPassword);
        }

        System.out.println(">>> 4. Database update result: " + updated);

        if (updated) {
            session.invalidate();
            resp.sendRedirect("login.jsp");
        } else {
            resp.getWriter().println("Failed to update. Email not found in DB.");
        }
    }
}