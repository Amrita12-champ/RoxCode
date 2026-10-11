<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.worksync.model.Attendance" %>

<%
    List<Attendance> attendanceList =
            (List<Attendance>) request.getAttribute("attendanceList");

    String selectedDate =
            (String) request.getAttribute("selectedDate");

    String attendanceError =
            (String) session.getAttribute("attendanceError");

    session.removeAttribute("attendanceError");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Attendance Management | WorkSync</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/dashboard.css">

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/attendance.css">
</head>

<body>

<div class="attendance-page">

    <header class="attendance-header">
        <div>
            <p class="eyebrow">WORKSYNC / HUMAN RESOURCES</p>
            <h1>Attendance Management</h1>
            <p class="subtitle">
                Monitor daily attendance and employee working hours.
            </p>
        </div>

        <a class="back-button"
           href="${pageContext.request.contextPath}/dashboard">
            Back to Dashboard
        </a>
    </header>

    <% if (attendanceError != null) { %>
    <div class="error-message">
        <%= attendanceError %>
    </div>
    <% } %>

    <section class="attendance-panel">

        <form method="get"
              action="${pageContext.request.contextPath}/attendance"
              class="date-filter">

            <div>
                <label for="date">Select Attendance Date</label>

                <input type="date"
                       id="date"
                       name="date"
                       value="<%= selectedDate %>"
                       required>
            </div>

            <button type="submit" class="primary-button">
                View Attendance
            </button>

        </form>

        <div class="table-container">

            <table>
                <thead>
                <tr>
                    <th>Employee ID</th>
                    <th>Employee Name</th>
                    <th>Department</th>
                    <th>Status</th>
                    <th>Check In</th>
                    <th>Check Out</th>
                    <th>Remarks</th>
                    <th>Action</th>
                </tr>
                </thead>

                <tbody>

                <% if (attendanceList != null && !attendanceList.isEmpty()) {
                    for (Attendance attendance : attendanceList) { %>

                <tr>
                    <td><%= attendance.getEmployeeId() %></td>

                    <td>
                        <%= attendance.getFirstName() %>
                        <%= attendance.getLastName() %>
                    </td>

                    <td>
                        <%= attendance.getDepartmentName() == null
                                ? "Unassigned"
                                : attendance.getDepartmentName() %>
                    </td>

                    <td>
                            <span class="status-badge">
                                <%= attendance.getStatus() %>
                            </span>
                    </td>

                    <td>
                        <%= attendance.getCheckIn() == null
                                ? "--"
                                : attendance.getCheckIn() %>
                    </td>

                    <td>
                        <%= attendance.getCheckOut() == null
                                ? "--"
                                : attendance.getCheckOut() %>
                    </td>

                    <td>
                        <%= attendance.getRemarks() == null
                                ? "--"
                                : attendance.getRemarks() %>
                    </td>

                    <td>
                        <button type="button"
                                class="edit-button"
                                onclick="openAttendanceForm(
                                        '<%= attendance.getEmployeeId() %>',
                                        '<%= attendance.getStatus() %>',
                                        '<%= attendance.getCheckIn() == null
                                            ? "" : attendance.getCheckIn().toString().substring(0, 5) %>',
                                        '<%= attendance.getCheckOut() == null
                                            ? "" : attendance.getCheckOut().toString().substring(0, 5) %>'
                                        )">
                            Edit
                        </button>
                    </td>
                </tr>

                <% }
                } else { %>

                <tr>
                    <td colspan="8" class="empty-state">
                        No attendance records found for this date.
                        Use the employee form below to add records.
                    </td>
                </tr>

                <% } %>

                </tbody>
            </table>

        </div>

    </section>

    <section class="attendance-panel form-panel">

        <h2>Mark or Update Attendance</h2>

        <form method="post"
              action="${pageContext.request.contextPath}/attendance"
              class="attendance-form">

            <div>
                <label for="employeeId">Employee ID</label>
                <input type="number"
                       id="employeeId"
                       name="employeeId"
                       min="1"
                       required>
            </div>

            <div>
                <label for="attendanceDate">Date</label>
                <input type="date"
                       id="attendanceDate"
                       name="attendanceDate"
                       value="<%= selectedDate %>"
                       required>
            </div>

            <div>
                <label for="status">Attendance Status</label>

                <select id="status" name="status" required>
                    <option value="PRESENT">Present</option>
                    <option value="ABSENT">Absent</option>
                    <option value="HALF_DAY">Half Day</option>
                    <option value="ON_LEAVE">On Leave</option>
                </select>
            </div>

            <div>
                <label for="checkIn">Check-In Time</label>
                <input type="time" id="checkIn" name="checkIn">
            </div>

            <div>
                <label for="checkOut">Check-Out Time</label>
                <input type="time" id="checkOut" name="checkOut">
            </div>

            <div>
                <label for="remarks">Remarks</label>
                <input type="text"
                       id="remarks"
                       name="remarks"
                       maxlength="255"
                       placeholder="Optional remarks">
            </div>

            <button type="submit" class="primary-button">
                Save Attendance
            </button>

        </form>

    </section>

</div>

<script>
    function openAttendanceForm(employeeId, status, checkIn, checkOut) {
        document.getElementById("employeeId").value = employeeId;
        document.getElementById("status").value = status;
        document.getElementById("checkIn").value = checkIn;
        document.getElementById("checkOut").value = checkOut;

        document.getElementById("attendanceDate").value =
            document.getElementById("date").value;

        document.querySelector(".form-panel").scrollIntoView({
            behavior: "smooth"
        });
    }
</script>

</body>
</html>