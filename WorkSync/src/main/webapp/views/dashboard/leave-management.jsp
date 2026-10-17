<%@ page import="java.util.List" %>
<%@ page import="com.worksync.model.LeaveRequest" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<LeaveRequest> leaveRequests =
            (List<LeaveRequest>) request.getAttribute("leaveRequests");

    if (leaveRequests == null) {
    leaveRequests = new java.util.ArrayList<>();
    }

    int totalRequests = leaveRequests.size();
    int pending = 0;
    int approved = 0;
    int rejected = 0;

    for (LeaveRequest leave : leaveRequests) {
    if ("PENDING".equalsIgnoreCase(leave.getStatus())) {
    pending++;
    } else if ("APPROVED".equalsIgnoreCase(leave.getStatus())) {
    approved++;
    } else if ("REJECTED".equalsIgnoreCase(leave.getStatus())) {
    rejected++;
    }
    }

    String contextPath = request.getContextPath();
    String username = (String) session.getAttribute("username");
    if (username == null) username = "Administrator";

    String role = (String) session.getAttribute("role");
    if (role == null) role = "SUPER ADMIN";


%>

<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>WorkSync | Leave Management</title>

    <link rel="stylesheet"
          href="<%= contextPath %>/css/dashboard.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/css/leave-management.css">

</head>

<body>

<div class="leave-layout">


    <!-- SIDEBAR -->
    <aside class="sidebar">

        <div class="sidebar-brand">
            <div class="brand-icon">W</div>
            <div>
                <h2>WorkSync</h2>
                <span>Enterprise Workspace</span>
            </div>
        </div>

        <div class="sidebar-label">WORKSPACE</div>

        <nav class="sidebar-nav">

            <a href="<%= contextPath %>/dashboard" class="nav-link">
                <span class="nav-icon">◫</span>
                <span>Dashboard</span>
            </a>

            <a href="<%= contextPath %>/employees" class="nav-link">
                <span class="nav-icon">♙</span>
                <span>Employees</span>
            </a>

            <a href="<%= contextPath %>/departments" class="nav-link">
                <span class="nav-icon">▦</span>
                <span>Departments</span>
            </a>

            <a href="<%= contextPath %>/attendance" class="nav-link">
                <span class="nav-icon">◷</span>
                <span>Attendance</span>
            </a>

            <a href="<%= contextPath %>/leave-management"
               class="nav-link active">
                <span class="nav-icon">▤</span>
                <span>Leave Management</span>
                <span class="nav-indicator"></span>
            </a>

        </nav>

        <div class="sidebar-bottom">
            <div class="sidebar-user">
                <div class="user-avatar">
                    <%= username.substring(0, 1).toUpperCase() %>
                </div>
                <div class="user-details">
                    <strong><%= username %></strong>
                    <span><%= role.replace("_", " ") %></span>
                </div>
            </div>

            <a href="<%= contextPath %>/logout" class="logout-link">
                <span class="nav-icon">↪</span>
                <span>Logout</span>
            </a>
        </div>

    </aside>

    <!-- MAIN CONTENT -->
    <main class="leave-main">

        <!-- TOPBAR -->
        <header class="leave-topbar">
            <div class="breadcrumb">
                <span>WORKSPACE</span>
                <span class="breadcrumb-separator">/</span>
                <span class="breadcrumb-current">LEAVE MANAGEMENT</span>
            </div>

            <div class="topbar-user">
                <div class="topbar-avatar">
                    <%= username.substring(0, 1).toUpperCase() %>
                </div>
                <div>
                    <strong><%= username %></strong>
                    <span>Administrator</span>
                </div>
            </div>
        </header>

        <!-- PAGE HEADING -->
        <section class="leave-page-heading">
            <div>
                <div class="eyebrow">
                    <span class="eyebrow-dot"></span>
                    WORKSYNC / WORKSPACE
                </div>

                <h1>Leave Management</h1>

                <p>
                    Manage employee leave applications, review requests,
                    and track approval status.
                </p>
            </div>

            <a href="<%= contextPath %>/dashboard"
               class="back-dashboard">
                <span>←</span>
                Back to Dashboard
            </a>
        </section>

        <!-- SUMMARY CARDS -->
        <section class="leave-stats">

            <div class="stat-card">
                <div class="stat-top">
                    <span>Total Requests</span>
                    <div class="stat-icon purple-icon">▤</div>
                </div>
                <h2><%= totalRequests %></h2>
                <p>All submitted applications</p>
            </div>

            <div class="stat-card">
                <div class="stat-top">
                    <span>Pending Requests</span>
                    <div class="stat-icon amber-icon">◷</div>
                </div>
                <h2 class="text-amber"><%= pending %></h2>
                <p>Awaiting a decision</p>
            </div>

            <div class="stat-card">
                <div class="stat-top">
                    <span>Approved Requests</span>
                    <div class="stat-icon green-icon">✓</div>
                </div>
                <h2 class="text-green"><%= approved %></h2>
                <p>Successfully approved</p>
            </div>

            <div class="stat-card">
                <div class="stat-top">
                    <span>Rejected Requests</span>
                    <div class="stat-icon red-icon">×</div>
                </div>
                <h2 class="text-red"><%= rejected %></h2>
                <p>Requests not approved</p>
            </div>

        </section>

        <!-- LEAVE REQUEST TABLE -->
        <section class="leave-panel">

            <div class="panel-heading">
                <div>
                    <h2>Leave Requests</h2>
                    <p>Review and manage employee applications.</p>
                </div>

                <div class="request-count">
                    <span class="count-dot"></span>
                    <%= totalRequests %> requests
                </div>
            </div>

            <!-- SEARCH AND FILTER -->
            <div class="leave-toolbar">
                <div class="search-box">
                    <span>⌕</span>
                    <input type="text"
                           id="leaveSearch"
                           placeholder="Search employee, department or leave type...">
                </div>

                <select id="statusFilter" class="status-filter">
                    <option value="ALL">All Statuses</option>
                    <option value="PENDING">Pending</option>
                    <option value="APPROVED">Approved</option>
                    <option value="REJECTED">Rejected</option>
                </select>
            </div>

            <div class="leave-table-wrapper">
                <table class="leave-table" id="leaveTable">

                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>EMPLOYEE</th>
                        <th>DEPARTMENT</th>
                        <th>LEAVE TYPE</th>
                        <th>START DATE</th>
                        <th>END DATE</th>
                        <th>REASON</th>
                        <th>STATUS</th>
                        <th>ACTION</th>
                    </tr>
                    </thead>

                    <tbody>

                    <% if (leaveRequests.isEmpty()) { %>

                    <tr id="emptyRow">
                        <td colspan="9" class="empty-state">
                            <div class="empty-icon">▤</div>
                            <strong>No leave requests yet</strong>
                            <p>
                                Leave applications will appear here
                                when employees submit them.
                            </p>
                        </td>
                    </tr>

                    <% } else {
                        for (LeaveRequest leave : leaveRequests) {
                        String employeeName =
                        (leave.getFirstName() == null ? "" : leave.getFirstName())
                        + " "
                        + (leave.getLastName() == null ? "" : leave.getLastName());

                        String department =
                        leave.getDepartmentName() == null
                        ? "Unassigned"
                        : leave.getDepartmentName();

                        String status = leave.getStatus() == null
                        ? "PENDING"
                        : leave.getStatus();
                    %>

                    <tr class="leave-row"
                        data-status="<%= status %>">

                        <td class="leave-id">
                            #<%= leave.getLeaveId() %>
                        </td>

                        <td>
                            <div class="employee-cell">
                                <div class="employee-avatar">
                                    <%= employeeName.trim().isEmpty()
                                        ? "?"
                                        : employeeName.trim().substring(0, 1).toUpperCase() %>
                                </div>
                                <strong><%= employeeName.trim() %></strong>
                            </div>
                        </td>

                        <td><span class="department-tag"><%= department %></span></td>

                        <td><%= leave.getLeaveType() %></td>

                        <td><%= leave.getStartDate() %></td>

                        <td><%= leave.getEndDate() %></td>

                        <td class="reason-cell"
                            title="<%= leave.getReason() == null ? "" : leave.getReason() %>">
                            <%= leave.getReason() == null || leave.getReason().trim().isEmpty()
                                ? "—"
                                : leave.getReason() %>
                        </td>

                        <td>
                        <span class="status-badge status-<%= status.toLowerCase() %>">
                            <span></span>
                            <%= status %>
                        </span>
                        </td>

                        <td>
                            <% if ("PENDING".equalsIgnoreCase(status)) { %>

                            <div class="action-buttons">

                                <form method="post"
                                      action="<%= contextPath %>/leave-management"
                                      onsubmit="return confirm('Approve this leave request?');">

                                    <input type="hidden"
                                           name="leaveId"
                                           value="<%= leave.getLeaveId() %>">

                                    <input type="hidden"
                                           name="status"
                                           value="APPROVED">

                                    <button type="submit"
                                            class="action-btn approve-btn">
                                        ✓ Approve
                                    </button>
                                </form>

                                <form method="post"
                                      action="<%= contextPath %>/leave-management"
                                      onsubmit="return confirm('Reject this leave request?');">

                                    <input type="hidden"
                                           name="leaveId"
                                           value="<%= leave.getLeaveId() %>">

                                    <input type="hidden"
                                           name="status"
                                           value="REJECTED">

                                    <button type="submit"
                                            class="action-btn reject-btn">
                                        × Reject
                                    </button>
                                </form>

                            </div>

                            <% } else { %>

                            <span class="reviewed-label">Reviewed</span>

                            <% } %>
                        </td>

                    </tr>

                    <% } } %>

                    <tr id="noSearchResults" style="display:none;">
                        <td colspan="9" class="no-results">
                            No requests match your search.
                        </td>
                    </tr>

                    </tbody>

                </table>
            </div>

            <div class="table-footer">
                <span>WorkSync Leave Management</span>
                <span>Secure employee leave records</span>
            </div>

        </section>

        <footer class="leave-footer">
            WorkSync · Enterprise Management System
        </footer>

    </main>
    ```

</div>

<script>
    const searchInput = document.getElementById("leaveSearch");
    const statusFilter = document.getElementById("statusFilter");
    const rows = document.querySelectorAll(".leave-row");
    const noResults = document.getElementById("noSearchResults");
    const emptyRow = document.getElementById("emptyRow");

    function filterLeaveRequests() {
        const search = searchInput.value.toLowerCase().trim();
        const selectedStatus = statusFilter.value;
        let visibleCount = 0;

        rows.forEach(function(row) {
            const matchesText = row.innerText.toLowerCase().includes(search);
            const matchesStatus = selectedStatus === "ALL"
                || row.dataset.status === selectedStatus;

            const visible = matchesText && matchesStatus;
            row.style.display = visible ? "" : "none";

            if (visible) visibleCount++;
        });

        noResults.style.display =
            rows.length > 0 && visibleCount === 0 ? "" : "none";

        if (emptyRow) {
            emptyRow.style.display = "";
        }
    }

    searchInput.addEventListener("input", filterLeaveRequests);
    statusFilter.addEventListener("change", filterLeaveRequests);
</script>

</body>
</html>
