package com.worksync.service;

import com.worksync.dao.DashboardDAO;

import java.sql.SQLException;

public class DashboardService {

    private final DashboardDAO dashboardDAO = new DashboardDAO();

    public int getTotalEmployees() throws SQLException {
        return dashboardDAO.getTotalEmployees();
    }

    public int getPresentToday() throws SQLException {
        return dashboardDAO.getPresentToday();
    }

    public int getPendingLeaveRequests() throws SQLException {
        return dashboardDAO.getPendingLeaveRequests();
    }

    public int getTotalDepartments() throws SQLException {
        return dashboardDAO.getTotalDepartments();
    }
}