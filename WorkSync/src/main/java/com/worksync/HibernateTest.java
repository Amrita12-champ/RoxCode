package com.worksync;

import com.worksync.util.HibernateUtil;
import org.hibernate.SessionFactory;

public class HibernateTest {
    public static void main(String[] args) {
        try {
            SessionFactory factory = HibernateUtil.getSessionFactory();

            System.out.println("WorkSync Hibernate Configuration Successful!");
            System.out.println("SessionFactory: " + factory);

        } catch (Exception e) {
            System.out.println("Hibernate configuration failed!");
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}