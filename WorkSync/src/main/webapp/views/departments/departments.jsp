```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.worksync.model.Department" %>
<%@ page import="com.worksync.constants.DepartmentStatus" %>

<%
    List<Department> departments =
            (List<Department>) request.getAttribute("departments");

    String search = (String) request.getAttribute("search");
    if (search == null) {
        search = "";
    }

    String contextPath = request.getContextPath();
    String error = (String) request.getAttribute("error");

    int activeCount = 0;

    if (departments != null) {
        for (Department d : departments) {
            if (d.getStatus() == DepartmentStatus.ACTIVE) {
                activeCount++;
            }
        }
    }

    String safeSearch = search.replace("&", "&amp;")
            .replace("\"", "&quot;")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Department Management | WorkSync</title>

    <link rel="stylesheet"
          href="<%= contextPath %>/css/dashboard.css">

    <link rel="stylesheet"
          href="<%= contextPath %>/css/departments.css">
</head>

<body>

<div class="app-layout">

    <!-- Sidebar -->
    <aside class="sidebar">

        <div class="brand">
            <div class="brand-icon">W</div>
            <h1>Work<span>Sync</span></h1>
        </div>

        <div class="nav-heading">WORKSPACE</div>

        <nav class="navigation">

            <a href="<%= contextPath %>/dashboard" class="nav-link">
                <span class="nav-icon">▦</span>
                <span>Dashboard</span>
            </a>

            <a href="<%= contextPath %>/employees" class="nav-link">
                <span class="nav-icon">♙</span>
                <span>Employees</span>
            </a>

            <a href="<%= contextPath %>/departments"
               class="nav-link active">
                <span class="nav-icon">▤</span>
                <span>Departments</span>
            </a>

            <a href="#" class="nav-link">
                <span class="nav-icon">◷</span>
                <span>Attendance</span>
            </a>

            <a href="#" class="nav-link">
                <span class="nav-icon">▧</span>
                <span>Leave Management</span>
            </a>

            <a href="#" class="nav-link">
                <span class="nav-icon">▥</span>
                <span>Reports &amp; Analytics</span>
            </a>

        </nav>

        <div class="sidebar-bottom">

            <div class="nav-heading">SYSTEM</div>

            <a href="#" class="nav-link">
                <span class="nav-icon">⚙</span>
                <span>Settings</span>
            </a>

            <a href="<%= contextPath %>/logout"
               class="nav-link logout-link">
                <span class="nav-icon">⇥</span>
                <span>Logout</span>
            </a>

        </div>

        <div class="dashboard-sidebar-footer">
            <div class="footer-avatar">WS</div>
            <div>
                <strong>WorkSync Admin</strong>
                <small>System Administrator</small>
            </div>
        </div>

    </aside>

    <!-- Main content -->
    <main class="main-content">

        <!-- Header -->
        <header class="topbar">

            <div class="page-heading">
                <div class="breadcrumb">WORKSPACE / DEPARTMENTS</div>
                <h2>Department Management</h2>
                <p class="page-subtitle">
                    Manage company departments and their operational status.
                </p>
            </div>

            <div class="user-info">
                <div class="user-avatar">A</div>
                <div class="user-details">
                    <strong>WorkSync Admin</strong>
                    <span>Super Admin</span>
                </div>
            </div>

        </header>

        <!-- Page content -->
        <section class="department-content">

            <!-- Page title and action -->
            <div class="section-heading department-page-heading">
                <div>
                    <h2>Departments</h2>
                    <p>Organize teams and manage your company's departments.</p>
                </div>

                <a class="primary-button"
                   href="<%= contextPath %>/departments?action=add">
                    <span>＋</span> Add Department
                </a>
            </div>

            <!-- Success and error messages -->
            <%
                if ("true".equals(request.getParameter("added"))) {
            %>
            <div class="department-alert alert-success">
                Department added successfully.
            </div>
            <%
            } else if ("true".equals(request.getParameter("updated"))) {
            %>
            <div class="department-alert alert-success">
                Department information updated successfully.
            </div>
            <%
                }

                if (error != null) {
            %>
            <div class="department-alert alert-error">
                <%= error %>
            </div>
            <%
                }
            %>

            <!-- Statistics -->
            <div class="stats-grid department-stats">

                <div class="stat-card">
                    <div class="stat-top">
                        <span class="stat-label">Total Departments</span>
                        <div class="stat-icon department-icon">▤</div>
                    </div>

                    <h3><%= departments == null ? 0 : departments.size() %></h3>
                    <p class="stat-description">Departments in this view</p>
                </div>

                <div class="stat-card">
                    <div class="stat-top">
                        <span class="stat-label">Active Departments</span>
                        <div class="stat-icon department-icon">✓</div>
                    </div>

                    <h3><%= activeCount %></h3>
                    <p class="stat-description">Currently operational</p>
                </div>

                <div class="stat-card">
                    <div class="stat-top">
                        <span class="stat-label">Inactive Departments</span>
                        <div class="stat-icon leave-icon">◷</div>
                    </div>

                    <h3>
                        <%= departments == null ? 0 :
                                departments.size() - activeCount %>
                    </h3>

                    <p class="stat-description">Currently inactive</p>
                </div>

            </div>

            <!-- Department table card -->
            <div class="content-card department-table-card">

                <div class="card-heading department-table-heading">

                    <div>
                        <h3>All Departments</h3>
                        <p>View, search, and manage company departments.</p>
                    </div>

                    <form class="department-search-form"
                          action="<%= contextPath %>/departments"
                          method="get">

                        <input type="text"
                               name="search"
                               class="department-search-input"
                               placeholder="Search departments..."
                               value="<%= safeSearch %>">

                        <button type="submit" class="primary-button">
                            Search
                        </button>

                    </form>

                </div>

                <div class="department-table-wrapper">

                    <table class="department-table">

                        <thead>
                        <tr>
                            <th>DEPARTMENT</th>
                            <th>DESCRIPTION</th>
                            <th>STATUS</th>
                            <th>ACTIONS</th>
                        </tr>
                        </thead>

                        <tbody>

                        <%
                            if (departments != null && !departments.isEmpty()) {
                                for (Department department : departments) {

                                    String departmentName =
                                            department.getDepartmentName() == null
                                                    ? ""
                                                    : department.getDepartmentName();

                                    String description =
                                            department.getDescription() == null
                                                    || department.getDescription().trim().isEmpty()
                                                    ? "No description provided"
                                                    : department.getDescription();

                                    String status =
                                            department.getStatus() == null
                                                    ? "ACTIVE"
                                                    : department.getStatus().name();

                                    String nextStatus =
                                            "ACTIVE".equals(status)
                                                    ? "INACTIVE"
                                                    : "ACTIVE";

                                    String safeName = departmentName
                                            .replace("&", "&amp;")
                                            .replace("<", "&lt;")
                                            .replace(">", "&gt;")
                                            .replace("\"", "&quot;");

                                    String safeDescription = description
                                            .replace("&", "&amp;")
                                            .replace("<", "&lt;")
                                            .replace(">", "&gt;")
                                            .replace("\"", "&quot;");
                        %>

                        <tr>
                            <td>
                                <div class="department-info">

                                    <div class="department-avatar">
                                        <%= departmentName.isEmpty()
                                                ? "D"
                                                : departmentName.substring(0, 1).toUpperCase() %>
                                    </div>

                                    <div class="department-details">
                                        <strong><%= safeName %></strong>
                                        <small>
                                            DEP-<%= String.format("%03d",
                                                department.getDepartmentId()) %>
                                        </small>
                                    </div>

                                </div>
                            </td>

                            <td class="description-cell">
                                <%= safeDescription %>
                            </td>

                            <td>
                                <span class="status-badge <%= "ACTIVE".equals(status)
                                        ? "status-active"
                                        : "status-inactive" %>">
                                    <%= status %>
                                </span>
                            </td>

                            <td>
                                <div class="department-actions">

                                    <a class="department-edit-button"
                                       href="<%= contextPath %>/departments?action=edit&id=<%= department.getDepartmentId() %>">
                                        Edit
                                    </a>

                                    <a class="department-status-button"
                                       href="<%= contextPath %>/departments?action=toggleStatus&id=<%= department.getDepartmentId() %>&amp;status=<%= nextStatus %>"
                                       onclick="return confirm('Change this department status?');">
                                        <%= "ACTIVE".equals(status)
                                                ? "Deactivate"
                                                : "Activate" %>
                                    </a>

                                </div>
                            </td>
                        </tr>

                        <%
                            }
                        } else {
                        %>

                        <tr>
                            <td colspan="4">
                                <div class="department-empty-state">
                                    <div class="department-empty-icon">▤</div>
                                    <h3>No departments found</h3>
                                    <p>Add a department or try a different search.</p>
                                </div>
                            </td>
                        </tr>

                        <%
                            }
                        %>

                        </tbody>

                    </table>

                </div>

                <div class="department-table-footer">
                    Showing <strong><%= departments == null ? 0 : departments.size() %></strong>
                    department(s)
                </div>

            </div>

            <footer class="dashboard-footer">
                <span>© 2026 WorkSync ERP</span>
                <span>Department Management</span>
            </footer>

        </section>

    </main>

</div>

</body>
</html>

