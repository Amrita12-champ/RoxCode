<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

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

    private String displayValue(Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            return "Not provided";
        }

        return escapeHtml(value);
    }
%>

<%
    if (session == null || session.getAttribute("userId") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String firstName = escapeHtml(request.getAttribute("firstName"));
    String lastName = escapeHtml(request.getAttribute("lastName"));
    String email = displayValue(request.getAttribute("email"));
    String phone = displayValue(request.getAttribute("phone"));
    String jobTitle = displayValue(request.getAttribute("jobTitle"));
    String departmentName = displayValue(request.getAttribute("departmentName"));
    String employmentStatus = displayValue(request.getAttribute("employmentStatus"));
    String joiningDate = displayValue(request.getAttribute("joiningDate"));

    String fullName = (firstName + " " + lastName).trim();

    if (fullName.isEmpty()) {
        fullName = "Employee";
    }

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

    int employeeId = request.getAttribute("employeeId") == null
            ? 0
            : ((Number) request.getAttribute("employeeId")).intValue();

    int totalAttendanceRecords = request.getAttribute("totalAttendanceRecords") == null
            ? 0 : ((Number) request.getAttribute("totalAttendanceRecords")).intValue();

    int presentDays = request.getAttribute("presentDays") == null
            ? 0 : ((Number) request.getAttribute("presentDays")).intValue();

    int absentDays = request.getAttribute("absentDays") == null
            ? 0 : ((Number) request.getAttribute("absentDays")).intValue();

    int halfDays = request.getAttribute("halfDays") == null
            ? 0 : ((Number) request.getAttribute("halfDays")).intValue();

    int totalLeaves = request.getAttribute("totalLeaves") == null
            ? 0 : ((Number) request.getAttribute("totalLeaves")).intValue();

    int pendingLeaves = request.getAttribute("pendingLeaves") == null
            ? 0 : ((Number) request.getAttribute("pendingLeaves")).intValue();

    int approvedLeaves = request.getAttribute("approvedLeaves") == null
            ? 0 : ((Number) request.getAttribute("approvedLeaves")).intValue();

    int rejectedLeaves = request.getAttribute("rejectedLeaves") == null
            ? 0 : ((Number) request.getAttribute("rejectedLeaves")).intValue();
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

            <a href="#leave-summary" class="nav-link">
                <span class="nav-icon">▤</span>
                <span>Leave Summary</span>
            </a>

            <a href="<%= request.getContextPath() %>/apply-leave"
               class="nav-link">
                <span class="nav-icon">＋</span>
                <span>Apply for Leave</span>
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

            <!-- Welcome section -->

            <div class="employee-welcome">

                <div>
                    <span class="welcome-label">
                        EMPLOYEE WORKSPACE
                    </span>

                    <h2>
                        Welcome back, <%= firstName.isEmpty()
                            ? "Employee" : firstName %>!
                    </h2>

                    <p>
                        Here's your personal WorkSync workspace.
                    </p>
                </div>

                <div class="welcome-symbol">✦</div>

            </div>

            <!-- Attendance summary -->

            <div id="attendance" class="employee-section-heading">

                <h3>Attendance Overview</h3>

                <p>
                    Your attendance records for the current month.
                </p>

            </div>

            <div class="employee-service-grid statistics-grid">

                <section class="content-card statistic-card">
                    <div class="service-icon attendance-icon">◷</div>
                    <span class="statistic-label">Present Days</span>
                    <strong class="statistic-number">
                        <%= presentDays %>
                    </strong>
                    <span class="statistic-note">Current month</span>
                </section>

                <section class="content-card statistic-card">
                    <div class="service-icon absent-icon">×</div>
                    <span class="statistic-label">Absent Days</span>
                    <strong class="statistic-number">
                        <%= absentDays %>
                    </strong>
                    <span class="statistic-note">Current month</span>
                </section>

                <section class="content-card statistic-card">
                    <div class="service-icon halfday-icon">◐</div>
                    <span class="statistic-label">Half Days</span>
                    <strong class="statistic-number">
                        <%= halfDays %>
                    </strong>
                    <span class="statistic-note">Current month</span>
                </section>

                <section class="content-card statistic-card">
                    <div class="service-icon leave-service-icon">▤</div>
                    <span class="statistic-label">Total Records</span>
                    <strong class="statistic-number">
                        <%= totalAttendanceRecords %>
                    </strong>
                    <span class="statistic-note">Current month</span>
                </section>

            </div>

            <!-- Leave summary -->

            <div id="leave-summary" class="employee-section-heading">

                <h3>Leave Overview</h3>

                <p>
                    Track the status of your leave applications.
                </p>

            </div>

            <div class="employee-service-grid statistics-grid">

                <section class="content-card statistic-card">
                    <div class="service-icon pending-icon">◷</div>
                    <span class="statistic-label">Pending Requests</span>
                    <strong class="statistic-number">
                        <%= pendingLeaves %>
                    </strong>
                    <span class="statistic-note">Awaiting review</span>
                </section>

                <section class="content-card statistic-card">
                    <div class="service-icon approved-icon">✓</div>
                    <span class="statistic-label">Approved Requests</span>
                    <strong class="statistic-number">
                        <%= approvedLeaves %>
                    </strong>
                    <span class="statistic-note">Approved applications</span>
                </section>

                <section class="content-card statistic-card">
                    <div class="service-icon rejected-icon">×</div>
                    <span class="statistic-label">Rejected Requests</span>
                    <strong class="statistic-number">
                        <%= rejectedLeaves %>
                    </strong>
                    <span class="statistic-note">Rejected applications</span>
                </section>

                <section class="content-card statistic-card">
                    <div class="service-icon leave-service-icon">▤</div>
                    <span class="statistic-label">Total Applications</span>
                    <strong class="statistic-number">
                        <%= totalLeaves %>
                    </strong>
                    <span class="statistic-note">All leave requests</span>
                </section>

            </div>

            <!-- Employee profile -->

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
                        <strong><%= employeeId %></strong>
                    </div>

                    <div class="profile-item">
                        <span>Email Address</span>
                        <strong><%= email %></strong>
                    </div>

                    <div class="profile-item">
                        <span>Phone Number</span>
                        <strong><%= phone %></strong>
                    </div>

                    <div class="profile-item">
                        <span>Job Title</span>
                        <strong><%= jobTitle %></strong>
                    </div>

                    <div class="profile-item">
                        <span>Department</span>
                        <strong><%= departmentName %></strong>
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

            <!-- Quick actions -->

            <div class="employee-section-heading">

                <h3>Quick Actions</h3>

                <p>
                    Access your employee services.
                </p>

            </div>

            <div class="employee-service-grid">

                <section class="content-card service-card">

                    <div class="service-icon attendance-icon">◷</div>

                    <h3>My Attendance</h3>

                    <p>
                        Review your current month's attendance statistics
                        above.
                    </p>

                    <a href="#attendance" class="leave-action-link">
                        View Attendance Summary <span>→</span>
                    </a>

                </section>

                <section class="content-card service-card">

                    <div class="service-icon leave-service-icon">▤</div>

                    <h3>Leave Management</h3>

                    <p>
                        Submit a leave application and check your request
                        statistics.
                    </p>

                    <a href="<%= request.getContextPath() %>/apply-leave"
                       class="leave-action-link">
                        Apply for Leave <span>→</span>
                    </a>

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