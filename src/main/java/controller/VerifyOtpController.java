package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/verify-otp")
public class VerifyOtpController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String enteredOtp = req.getParameter("otp");

        HttpSession session = req.getSession();

        Integer generatedOtp = (Integer) session.getAttribute("otp");

        if (generatedOtp != null &&
                generatedOtp.toString().equals(enteredOtp)) {

            resp.sendRedirect("reset-password.html");

        } else {

            resp.getWriter().println("Invalid OTP");
        }
    }
}