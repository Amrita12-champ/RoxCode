package controller;

import dao.StudentDb;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name=req.getParameter("fullName");
        String email=req.getParameter("email");
        String password=req.getParameter("password");
        System.out.println(name+""+email+""+password);
        Student s1=new Student();
        s1.setName(name);
        s1.setEmail(email);
        s1.setPassword(password);
        StudentDb db= new StudentDb();
        db.connection();
        db.insert(s1);

    }
}
