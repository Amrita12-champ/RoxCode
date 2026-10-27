<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>WorkSync | Reports & Analytics</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/dashboard.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/reports.css">

</head>

<body>

<div class="app-layout">

    <aside class="sidebar">

        <div class="brand">
            <div class="brand-icon">W</div>
            <h1>Work<span>Sync</span></h1>
        </div>

        <p class="nav-heading">WORKSPACE</p>

        <nav class="navigation">

            <a class="nav-link"
               href="${pageContext.request.contextPath}/dashboard">
                <span class="nav-icon">▦</span>
                Dashboard
            </a>

            <a class="nav-link"
               href="${pageContext.request.contextPath}/employees">
                <span class="nav-icon">♙</span>
                Employees
            </a>

            <a class="nav-link"
               href="${pageContext.request.contextPath}/departments">
                <span class="nav-icon">▤</span>
                Departments
            </a>

            <a class="nav-link"
               href="${pageContext.request.contextPath}/attendance">
                <span class="nav-icon">◷</span>
                Attendance
            </a>

            <a class="nav-link"
               href="${pageContext.request.contextPath}/leave-management">
                <span class="nav-icon">▧</span>
                Leave Management
            </a>

            <a class="nav-link active"
               href="${pageContext.request.contextPath}/reports">
                <span class="nav-icon">▥</span>
                Reports
            </a>

        </nav>

        <div class="sidebar-bottom">

            <div class="help-card">
                <div class="help-icon">✦</div>
                <h3>WorkSync Workspace</h3>
                <p>Your workforce, organized in one place.</p>
            </div>

            <a class="nav-link logout-link"
               href="${pageContext.request.contextPath}/logout">
                <span class="nav-icon">↪</span>
                Logout
            </a>

        </div>

    </aside>

    <main class="main-content">

        <header class="topbar">

            <div class="page-heading">
                <p class="breadcrumb">WORKSPACE / REPORTS</p>
                <h2>Reports & Analytics</h2>
                <p class="page-subtitle">
                    Understand your workforce operations using live data.
                </p>
            </div>

            <div class="user-info">

                <div class="user-avatar">
                    <c:out value="${sessionScope.username}"/>
                </div>

                <div class="user-details">
                    <strong>
                        <c:out value="${sessionScope.username}"/>
                    </strong>
                    <span>
                        <c:out value="${sessionScope.role}"/>
                    </span>
                </div>

            </div>

        </header>

        <!-- Date filters -->

        <section class="report-filter-card">

            <div>
                <h3>Report Period</h3>
                <p>Select dates to filter attendance and leave reports.</p>
            </div>

            <form method="get"
                  action="${pageContext.request.contextPath}/reports"
                  class="report-filter-form">

                <label>
                    Start Date
                    <input type="date"
                           name="startDate"
                           value="<c:out value='${startDate}'/>"
                           required>
                </label>

                <label>
                    End Date
                    <input type="date"
                           name="endDate"
                           value="<c:out value='${endDate}'/>"
                           required>
                </label>

                <button type="submit" class="report-button">
                    Apply Filters
                </button>

            </form>

        </section>

        <!-- Workforce report -->

        <section class="section-heading">
            <div>
                <h2>Workforce Overview</h2>
                <p>Current employee and department information.</p>
            </div>
        </section>

        <section class="stats-grid">

            <article class="stat-card">
                <div class="stat-top">
                    <div class="stat-icon employees-icon">♙</div>
                    <span class="stat-tag">PEOPLE</span>
                </div>
                <p class="stat-label">Total Employees</p>
                <h3><c:out value="${totalEmployees}"/></h3>
                <p class="stat-description">Registered employees</p>
            </article>

            <article class="stat-card">
                <div class="stat-top">
                    <div class="stat-icon attendance-icon">✓</div>
                    <span class="stat-tag">ACTIVE</span>
                </div>
                <p class="stat-label">Active Employees</p>
                <h3><c:out value="${activeEmployees}"/></h3>
                <p class="stat-description">Currently active employees</p>
            </article>

            <article class="stat-card">
                <div class="stat-top">
                    <div class="stat-icon department-icon">▤</div>
                    <span class="stat-tag">TEAMS</span>
                </div>
                <p class="stat-label">Departments</p>
                <h3><c:out value="${totalDepartments}"/></h3>
                <p class="stat-description">Registered departments</p>
            </article>

        </section>

        <!-- Attendance report -->

        <section class="section-heading">
            <div>
                <h2>Attendance Report</h2>
                <p>Attendance records within your selected date range.</p>
            </div>

            <a class="report-button"
               href="${pageContext.request.contextPath}/reports?startDate=${startDate}&amp;endDate=${endDate}&amp;export=csv">
                Export CSV
            </a>
        </section>

        <section class="stats-grid">

            <article class="stat-card">
                <p class="stat-label">Total Records</p>
                <h3><c:out value="${totalAttendance}"/></h3>
                <p class="stat-description">Attendance entries</p>
            </article>

            <article class="stat-card">
                <p class="stat-label">Present</p>
                <h3><c:out value="${presentCount}"/></h3>
                <p class="stat-description">Present records</p>
            </article>

            <article class="stat-card">
                <p class="stat-label">Absent</p>
                <h3><c:out value="${absentCount}"/></h3>
                <p class="stat-description">Absent records</p>
            </article>

            <article class="stat-card">
                <p class="stat-label">Half Day</p>
                <h3><c:out value="${halfDayCount}"/></h3>
                <p class="stat-description">Half-day records</p>
            </article>

            <article class="stat-card">
                <p class="stat-label">On Leave</p>
                <h3><c:out value="${onLeaveCount}"/></h3>
                <p class="stat-description">Attendance marked on leave</p>
            </article>

        </section>

        <!-- Leave report -->

        <section class="section-heading">
            <div>
                <h2>Leave Report</h2>
                <p>Leave applications submitted during the selected period.</p>
            </div>
        </section>

        <section class="stats-grid">

            <article class="stat-card">
                <p class="stat-label">Total Requests</p>
                <h3><c:out value="${totalLeaveRequests}"/></h3>
                <p class="stat-description">Submitted applications</p>
            </article>

            <article class="stat-card">
                <p class="stat-label">Pending</p>
                <h3><c:out value="${pendingLeaves}"/></h3>
                <p class="stat-description">Awaiting review</p>
            </article>

            <article class="stat-card">
                <p class="stat-label">Approved</p>
                <h3><c:out value="${approvedLeaves}"/></h3>
                <p class="stat-description">Approved applications</p>
            </article>

            <article class="stat-card">
                <p class="stat-label">Rejected</p>
                <h3><c:out value="${rejectedLeaves}"/></h3>
                <p class="stat-description">Rejected applications</p>
            </article>

        </section>

        <!-- Department report -->

        <section class="content-card report-table-card">

            <div class="card-heading">
                <div>
                    <h3>Employees by Department</h3>
                    <p>Workforce distribution across departments.</p>
                </div>
            </div>

            <div class="table-wrapper">

                <table class="report-table">

                    <thead>
                    <tr>
                        <th>Department</th>
                        <th>Employee Count</th>
                    </tr>
                    </thead>

                    <tbody>

                    <c:forEach var="department"
                               items="${departmentReport}">

                        <tr>
                            <td>
                                <c:out value="${department.departmentName}"/>
                            </td>

                            <td>
                                <span class="employee-count-badge">
                                    <c:out value="${department.employeeCount}"/>
                                </span>
                            </td>
                        </tr>

                    </c:forEach>

                    <c:if test="${empty departmentReport}">
                        <tr>
                            <td colspan="2" class="empty-message">
                                No department records found.
                            </td>
                        </tr>
                    </c:if>

                    </tbody>

                </table>

            </div>

        </section>