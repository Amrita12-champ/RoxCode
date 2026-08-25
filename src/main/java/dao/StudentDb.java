package dao;

import entity.Admin;
import entity.Student;

import java.sql.*;

public class StudentDb {
        private static final String url="jdbc:mysql://localhost:3306/RoxCode";
        private static final String user="root";
        private static final String pass="Satya@2005";
        private Connection con=null;

        public void connection(){
            try {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                con= DriverManager.getConnection(url,user,pass);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        public int insert(Student a){
            String query="insert into register values (?,?,?)";
            try {
                PreparedStatement prt=con.prepareStatement(query);
                prt.setString(1,a.getName());
                prt.setString(2,a.getEmail());
                prt.setString(3, a.getPassword());

                return prt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        public boolean delete(String email){
            String query="delete from register where email=?";
            try {
                PreparedStatement prt=con.prepareStatement(query);
                prt.setString(1,email);
                if(prt.executeUpdate()>0){
                    return true;
                }
                return false;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        public int update(Student a){
            String query="update register set name=?, Pass=? where email=?";
            try {
                PreparedStatement prt=con.prepareStatement(query);
                prt.setString(1,a.getName());
                prt.setString(2,a.getPassword());
                prt.setString(3,a.getEmail());
                return prt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        public String search(String email){
            String sql="select*from register where email=?";
            try {
                PreparedStatement prt=con.prepareStatement(sql);
                prt.setString(1,email);
                ResultSet rs= prt.executeQuery();
                if(rs.next()) {
                    return rs.getString(2);
                }
                else
                    return "Invalid credential";
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


        //Admin Databse


        public int insert(Admin a){
        String query="insert into Admin_register values (?,?,?)";
        try {
            PreparedStatement prt = con.prepareStatement(query);
            prt.setString(1,a.getName());
            prt.setString(2,a.getEmail());
            prt.setString(3, a.getPassword());

            return prt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
           }
        }
        public boolean delete(Admin a){
        String query="delete from Admin_register where email=?";
        try{
            PreparedStatement prt = con.prepareStatement(query);
                prt.setString(1, a.getEmail());
                return prt.executeUpdate()>0;
            }
         catch (SQLException e) {
            throw new RuntimeException(e);
         }
        }
        public int update(Admin a){
            String query="update Admin_register set name=?, Pass=? where email=?";
            try {
                PreparedStatement prt = con.prepareStatement(query);
                prt.setString(1,a.getName());
                prt.setString(2,a.getPassword());
                prt.setString(3,a.getEmail());


                return prt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        public String SearchAdm(String email) {
            String query="select * from Admin_register where email=?";
            try{
                PreparedStatement prt = con.prepareStatement(query);
                prt.setString(1,email);
                ResultSet rs = prt.executeQuery();
                if(rs.next()){
                    return rs.getString(2);
                } else {
                    return "Invalid";
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }


