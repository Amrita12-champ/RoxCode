
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Objects" %>

<%!
    private String escapeHtml(Object value) {
        if (value == null) {
            return "";
        }

        return value.toString()
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
%>

<%
    if (session == null || session.getAttribute("userId") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String firstName = escapeHtml(request.getAttribute("firstName"));
    String lastName = escapeHtml(request.getAttribute("lastName"));
    String email = escapeHtml(request.getAttribute("email"));
    String phone = escapeHtml(request.getAttribute("phone"));
    String jobTitle = escapeHtml(request.getAttribute("jobTitle"));
    String departmentName = escapeHtml(request.getAttribute("departmentName"));
    String employmentStatus = escapeHtml(request.getAttribute("employmentStatus"));
    String joiningDate = escapeHtml(request.getAttribute("joiningDate"));

    String fullName = (firstName + " " + lastName).trim();

    String initials = "";

    if (!firstName.isEmpty()) {
        initials += firstName.substring(0, 1).toUpperCase();
    }

    if (!lastName.isEmpty()) {
        initials += lastName.substring(0, 1).toUpperCase();
    }

    if (initials.isEmpty()) {
        initials = "E";
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Employee Dashboard | WorkSync</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/dashboard.css">

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/employee-dashboard.css">
</head>

<body>

<div class="app-layout">

    <aside class="sidebar">

        <div class="brand">
            <div class="brand-icon">W</div>
            <h1>WorkSync</h1>
        </div>

        <p class="nav-heading">EMPLOYEE WORKSPACE</p>

        <nav class="navigation">

            <a href="<%= request.getContextPath() %>/employee-dashboard"
               class="nav-link active">
                <span class="nav-icon">▦</span>
                <span>My Dashboard</span>
            </a>

            <a href="#profile" class="nav-link">
                <span class="nav-icon">♙</span>
                <span>My Profile</span>
            </a>

            <a href="#attendance" class="nav-link">
                <span class="nav-icon">◷</span>
                <span>My Attendance</span>
            </a>

            <a href="#leave" class="nav-link">
                <span class="nav-icon">▤</span>
                <span>Leave Management</span>
            </a>

        </nav>

        <div class="sidebar-bottom">
            <a href="<%= request.getContextPath() %>/logout"
               class="logout-link">
                <span class="nav-icon">⇥</span>
                <span>Logout</span>
            </a>
        </div>

    </aside>

    <main class="main-content">

        <header class="topbar">

            <div class="page-heading">
                <div class="breadcrumb">
                    WorkSync <span>/</span> Employee Workspace
                </div>

                <h2>My Dashboard</h2>

                <p class="page-subtitle">
                    Your work information, all in one place.
                </p>
            </div>

            <div class="user-info">

                <div class="user-avatar">
                    <%= initials %>
                </div>

                <div class="user-details">
                    <strong><%= fullName %></strong>
                    <span>Employee</span>
                </div>

            </div>

        </header>

        <section class="employee-content">

            <div class="employee-welcome">

                <div>
                    <span class="welcome-label">EMPLOYEE WORKSPACE</span>

                    <h2>
                        Welcome back, <%= firstName %>!
                    </h2>

                    <p>
                        Here's your personal WorkSync workspace.
                    </p>
                </div>

                <div class="welcome-symbol">✦</div>

            </div>

            <section id="profile" class="content-card profile-card">

                <div class="card-heading">
                    <div>
                        <h3>My Profile</h3>
                        <p>Your personal and employment information.</p>
                    </div>

                    <span class="employee-status">
                        <%= employmentStatus %>
                    </span>
                </div>

                <div class="profile-grid">

                    <div class="profile-item">
                        <span>Full Name</span>
                        <strong><%= fullName %></strong>
                    </div>

                    <div class="profile-item">
                        <span>Employee ID</span>
                        <strong><%= request.getAttribute("employeeId") %></strong>
                    </div>

                    <div class="profile-item">
                        <span>Email Address</span>
                        <strong><%= email %></strong>
                    </div>

                    <div class="profile-item">
                        <span>Phone Number</span>
                        <strong><%= phone.isEmpty() ? "Not provided" : phone %></strong>
                    </div>

                    <div class="profile-item">
                        <span>Job Title</span>
                        <strong><%= jobTitle %></strong>
                    </div>

                    <div class="profile-item">
                        <span>Department</span>
                        <strong>
                            <%= departmentName.isEmpty()
                                    ? "Not assigned"
                                    : departmentName %>
                        </strong>
                    </div>

                    <div class="profile-item">
                        <span>Joining Date</span>
                        <strong><%= joiningDate %></strong>
                    </div>

                    <div class="profile-item">
                        <span>Account Role</span>
                        <strong>Employee</strong>
                    </div>

                </div>

            </section>

            <div class="employee-section-heading">
                <h3>My Workspace</h3>
                <p>Quick access to your employee services.</p>
            </div>

            <div class="employee-service-grid">

                <section id="attendance" class="content-card service-card">
                    <div class="service-icon attendance-icon">◷</div>
                    <h3>My Attendance</h3>
                    <p>
                        Your attendance summary and daily attendance records
                        will appear here.
                    </p>
                    <span class="coming-soon">Attendance module coming next</span>
                </section>

                <section id="leave" class="content-card service-card">
                    <div class="service-icon leave-service-icon">▤</div>
                    <h3>Leave Management</h3>
                    <p>
                        Apply for leave and track your leave requests
                        and approval status.
                    </p>
                    <span class="coming-soon">Leave module coming soon</span>
                </section>

            </div>

            <footer class="employee-footer">
                WorkSync ERP &copy; 2026
                <span>Employee Self-Service Portal</span>
            </footer>

        </section>

    </main>

</div>

</body>
</html>

