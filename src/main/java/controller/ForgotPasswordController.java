package controller;

import dao.StudentDb;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Random;

@WebServlet("/forgot-password")
public class ForgotPasswordController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");

        StudentDb db = new StudentDb();

        boolean exists = db.emailExists(email);

        if (exists) {

            // Generate 6 digit OTP
            int otp = 100000 + new Random().nextInt(900000);

            // Store email and OTP in session
            HttpSession session = req.getSession();
            session.setAttribute("email", email);
            session.setAttribute("otp", otp);

            // Show OTP in IntelliJ console
            System.out.println("Generated OTP: " + otp);

            // Open OTP page
            resp.sendRedirect("verify-otp.html");

        } else {

            resp.getWriter().println("Email is not registered.");
        }
    }
}