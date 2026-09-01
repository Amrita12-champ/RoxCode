package controller;

import dao.StudentDb;
import entity.Admin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Admin_login")
public class AdminRegistrationController extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("fullname");
        String email =  request.getParameter("email");
        String password = request.getParameter("password");
        System.out.println(name + " " + email + " " + password);
        Admin a = new Admin();
        a.setName(name);
        a.setEmail(email);
        a.setPassword(password);
        StudentDb db = new StudentDb();
        db.connection();

        int res = db.insert(a);
        if(res>0){
            response.sendRedirect("login.html");
        }else {
            response.getWriter().println("Admin_login_failed");
        }


    }
}
