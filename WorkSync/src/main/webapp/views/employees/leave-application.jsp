<%@ page import="java.util.List" %>
<%@ page import="com.worksync.model.LeaveRequest" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String contextPath = request.getContextPath();

    String username = (String) session.getAttribute("username");
    if (username == null) username = "Employee";

    List<LeaveRequest> leaveRequests =
    (List<LeaveRequest>) request.getAttribute("leaveRequests");

    if (leaveRequests == null) {
    leaveRequests = new java.util.ArrayList<>();
    }

    String message = (String) session.getAttribute("leaveMessage");
    session.removeAttribute("leaveMessage");


%>

<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>WorkSync | My Leave</title>

    <link rel="stylesheet"
          href="<%= contextPath %>/css/dashboard.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/css/leave-management.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/css/leave-application.css">


</head>

<body>

<div class="employee-leave-layout">

    <aside class="sidebar">

        <div class="sidebar-brand">
            <div class="brand-icon">W</div>
            <div>
                <h2>WorkSync</h2>
                <span>Enterprise Workspace</span>
            </div>
        </div>

        <div class="sidebar-label">EMPLOYEE WORKSPACE</div>

        <nav class="sidebar-nav">

            <a href="<%= contextPath %>/employee-dashboard" class="nav-link">
                <span class="nav-icon">◫</span>
                <span>My Dashboard</span>
            </a>

            <a href="<%= contextPath %>/apply-leave"
               class="nav-link active">
                <span class="nav-icon">▤</span>
                <span>My Leave</span>
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
                    <span>Employee</span>
                </div>
            </div>

            <a href="<%= contextPath %>/logout" class="logout-link">
                <span class="nav-icon">↪</span>
                <span>Logout</span>
            </a>

        </div>

    </aside>

    <main class="employee-leave-main">

        <header class="leave-topbar">
            <div class="breadcrumb">
                <span>EMPLOYEE WORKSPACE</span>
                <span class="breadcrumb-separator">/</span>
                <span class="breadcrumb-current">MY LEAVE</span>
            </div>

            <div class="topbar-user">
                <div class="topbar-avatar">
                    <%= username.substring(0, 1).toUpperCase() %>
                </div>

                <div>
                    <strong><%= username %></strong>
                    <span>Employee Account</span>
                </div>
            </div>
        </header>

        <section class="employee-leave-heading">

            <div>
                <div class="eyebrow">
                    <span class="eyebrow-dot"></span>
                    WORKSYNC / EMPLOYEE
                </div>

                <h1>My Leave</h1>

                <p>
                    Apply for time off and keep track of your leave requests.
                </p>
            </div>

        </section>

        <% if (message != null) { %>
        <div class="leave-message">
            <%= message %>
        </div>
        <% } %>

        <section class="application-panel">

            <div class="application-panel-heading">
                <div>
                    <h2>Apply for Leave</h2>
                    <p>Complete the details below to submit a request.</p>
                </div>

                <div class="application-icon">＋</div>
            </div>

            <form action="<%= contextPath %>/apply-leave"
                  method="post"
                  class="leave-application-form">

                <div class="form-grid">

                    <div class="form-group">
                        <label for="leaveType">Leave Type <span>*</span></label>

                        <select id="leaveType" name="leaveType" required>
                            <option value="">Select leave type</option>
                            <option value="CASUAL">Casual Leave</option>
                            <option value="SICK">Sick Leave</option>
                            <option value="EARNED">Earned Leave</option>
                            <option value="UNPAID">Unpaid Leave</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="startDate">Start Date <span>*</span></label>

                        <input type="date"
                               id="startDate"
                               name="startDate"
                               required>
                    </div>

                    <div class="form-group">
                        <label for="endDate">End Date <span>*</span></label>

                        <input type="date"
                               id="endDate"
                               name="endDate"
                               required>
                    </div>

                    <div class="form-group full-width">
                        <label for="reason">Reason for Leave</label>

                        <textarea id="reason"
                                  name="reason"
                                  rows="4"
                                  maxlength="500"
                                  placeholder="Briefly explain why you need leave..."></textarea>

                        <div class="field-hint">
                            Maximum 500 characters.
                        </div>
                    </div>

                </div>

                <div class="form-footer">
                    <span>New requests are submitted as Pending.</span>

                    <button type="submit" class="submit-leave-btn">
                        Submit Application <span>→</span>
                    </button>
                </div>

            </form>

        </section>

        <section class="my-requests-panel">

            <div class="application-panel-heading">
                <div>
                    <h2>My Leave Requests</h2>
                    <p>Track the status of your submitted applications.</p>
                </div>

                <div class="my-request-count">
                    <%= leaveRequests.size() %> requests
                </div>
            </div>

            <div class="my-leave-table-wrapper">

                <table class="my-leave-table">

                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>LEAVE TYPE</th>
                        <th>START DATE</th>
                        <th>END DATE</th>
                        <th>REASON</th>
                        <th>STATUS</th>
                    </tr>
                    </thead>

                    <tbody>

                    <% if (leaveRequests.isEmpty()) { %>

                    <tr>
                        <td colspan="6" class="my-leave-empty">
                            No leave applications submitted yet.
                        </td>
                    </tr>

                    <% } else {
                        for (LeaveRequest leave : leaveRequests) {
                        String status = leave.getStatus() == null
                        ? "PENDING"
                        : leave.getStatus();
                    %>

                    <tr>
                        <td class="leave-id">
                            #<%= leave.getLeaveId() %>
                        </td>

                        <td><%= leave.getLeaveType() %></td>

                        <td><%= leave.getStartDate() %></td>

                        <td><%= leave.getEndDate() %></td>

                        <td class="my-reason">
                            <%= leave.getReason() == null
                                || leave.getReason().trim().isEmpty()
                                ? "—"
                                : leave.getReason() %>
                        </td>

                        <td>
                        <span class="status-badge status-<%= status.toLowerCase() %>">
                            <span></span>
                            <%= status %>
                        </span>
                        </td>
                    </tr>

                    <% } } %>

                    </tbody>

                </table>

            </div>

        </section>

        <footer class="leave-footer">
            WorkSync · Employee Workspace
        </footer>

    </main>
    ```

</div>

<script>
    const startInput = document.getElementById("startDate");
    const endInput = document.getElementById("endDate");

    const today = new Date();
    const localToday = [
        today.getFullYear(),
        String(today.getMonth() + 1).padStart(2, "0"),
        String(today.getDate()).padStart(2, "0")
    ].join("-");

    startInput.min = localToday;
    endInput.min = localToday;

    startInput.addEventListener("change", function() {
        endInput.min = startInput.value || localToday;

        if (endInput.value && endInput.value < startInput.value) {
            endInput.value = startInput.value;
        }
    });
</script>

</body>
</html>
