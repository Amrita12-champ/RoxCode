package dao;

import entity.Student;

import java.sql.*;

public class StudentDb {
        private static final String url="jdbc:postgresql://localhost:5432/RoxCode";
        private static final String user="postgres";
        private static final String pass="Cutm@059";
        private Connection con=null;

        public void connection(){
            try {
                try {
                    Class.forName("org.postgresql.Driver");
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
    }


