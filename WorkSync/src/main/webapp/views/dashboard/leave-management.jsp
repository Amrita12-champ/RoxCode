<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.worksync.model.LeaveRequest" %>

<%
    List<LeaveRequest> requests =
            (List<LeaveRequest>) request.getAttribute("leaveRequests");

    int pending = 0;
    int approved = 0;
    int rejected = 0;

    if (requests != null) {
        for (LeaveRequest leave : requests) {
            if ("PENDING".equals(leave.getStatus())) {
                pending++;
            } else if ("APPROVED".equals(leave.getStatus())) {
                approved++;
            } else if ("REJECTED".equals(leave.getStatus())) {
                rejected++;
            }
        }
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Leave Management | WorkSync</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/dashboard.css">

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/leave-management.css">
</head>

<body>

<div class="leave-page">

    <header class="leave-header">
        <div>
            <p class="eyebrow">WORKSYNC / HUMAN RESOURCES</p>
            <h1>Leave Management</h1>
            <p class="subtitle">
                Review employee leave requests and manage approvals.
            </p>
        </div>

        <a class="back-button"
           href="<%= request.getContextPath() %>/dashboard">
            Back to Dashboard
        </a>
    </header>

    <section class="leave-stats">

        <div class="leave-stat-card">
            <span>Total Requests</span>
            <h2><%= requests == null ? 0 : requests.size() %></h2>
        </div>

        <div class="leave-stat-card pending-card">
            <span>Pending</span>
            <h2><%= pending %></h2>
        </div>

        <div class="leave-stat-card approved-card">
            <span>Approved</span>
            <h2><%= approved %></h2>
        </div>

        <div class="leave-stat-card rejected-card">
            <span>Rejected</span>
            <h2><%= rejected %></h2>
        </div>

    </section>

    <section class="leave-panel">

        <div class="panel-heading">
            <div>
                <h2>Leave Requests</h2>
                <p>All employee leave applications</p>
            </div>
        </div>

        <div class="table-container">

            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Employee</th>
                    <th>Department</th>
                    <th>Leave Type</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Reason</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
                </thead>

                <tbody>

                <% if (requests != null && !requests.isEmpty()) {
                    for (LeaveRequest leave : requests) { %>

                <tr>
                    <td><%= leave.getLeaveId() %></td>

                    <td>
                        <strong>
                            <%= leave.getFirstName() %>
                            <%= leave.getLastName() %>
                        </strong>
                        <small>Employee #<%= leave.getEmployeeId() %></small>
                    </td>

                    <td>
                        <%= leave.getDepartmentName() == null
                                ? "Unassigned"
                                : leave.getDepartmentName() %>
                    </td>

                    <td><%= leave.getLeaveType() %></td>

                    <td><%= leave.getStartDate() %></td>

                    <td><%= leave.getEndDate() %></td>

                    <td>
                        <%= leave.getReason() == null
                                || leave.getReason().isEmpty()
                                ? "--" : leave.getReason() %>
                    </td>

                    <td>
                            <span class="leave-status <%= leave.getStatus().toLowerCase() %>">
                                <%= leave.getStatus() %>
                            </span>
                    </td>

                    <td>
                        <% if ("PENDING".equals(leave.getStatus())) { %>

                        <form method="post"
                              action="<%= request.getContextPath() %>/leave-management"
                              class="leave-action-form">

                            <input type="hidden"
                                   name="leaveId"
                                   value="<%= leave.getLeaveId() %>">

                            <button type="submit"
                                    name="status"
                                    value="APPROVED"
                                    class="approve-button">
                                Approve
                            </button>

                            <button type="submit"
                                    name="status"
                                    value="REJECTED"
                                    class="reject-button">
                                Reject
                            </button>

                        </form>

                        <% } else { %>

                        <span class="reviewed-label">
                                    Reviewed
                                </span>

                        <% } %>
                    </td>
                </tr>

                <%  }
                } else { %>

                <tr>
                    <td colspan="9" class="empty-state">
                        No leave requests have been submitted yet.
                    </td>
                </tr>

                <% } %>

                </tbody>
            </table>

        </div>

    </section>

</div>

</body>
</html>
