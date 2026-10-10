
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.worksync.model.Department" %>

<%
    String contextPath = request.getContextPath();

    Department department =
            (Department) request.getAttribute("department");

    String formAction = (String) request.getAttribute("formAction");

    boolean isEdit = "update".equalsIgnoreCase(formAction)
            && department != null;

    String pageTitle = isEdit ? "Edit Department" : "Add Department";

    String departmentName = "";
    String description = "";

    if (department != null) {
        departmentName = department.getDepartmentName() == null
                ? ""
                : department.getDepartmentName();

        description = department.getDescription() == null
                ? ""
                : department.getDescription();
    }

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

    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title><%= pageTitle %> | WorkSync</title>

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
                <div class="breadcrumb">WORKSPACE / DEPARTMENTS / <%= isEdit ? "EDIT" : "ADD NEW" %></div>

                <h2><%= pageTitle %></h2>

                <p class="page-subtitle">
                    <%= isEdit
                            ? "Update your department information."
                            : "Create a new department for your organization." %>
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

        <!-- Form content -->
        <section class="department-content">

            <div class="section-heading department-page-heading">

                <div>
                    <h2>Department Information</h2>
                    <p>Enter the details below to <%= isEdit ? "update" : "create" %> a department.</p>
                </div>

                <a href="<%= contextPath %>/departments"
                   class="secondary-button">
                    ← Back to Departments
                </a>

            </div>

            <% if (error != null) { %>
            <div class="department-alert alert-error">
                <%= error.replace("&", "&amp;")
                        .replace("<", "&lt;")
                        .replace(">", "&gt;") %>
            </div>
            <% } %>

            <!-- Department form card -->
            <div class="form-card department-form-card">

                <div class="department-form-intro">
                    <div class="department-form-icon">▤</div>

                    <div>
                        <h3><%= isEdit ? "Edit Department Details" : "New Department Details" %></h3>

                        <p>
                            Fields marked with <span class="required">*</span>
                            are required.
                        </p>
                    </div>
                </div>

                <div class="department-form-divider"></div>

                <form action="<%= contextPath %>/departments"
                      method="post">

                    <input type="hidden"
                           name="action"
                           value="<%= isEdit ? "update" : "create" %>">

                    <% if (isEdit) { %>
                    <input type="hidden"
                           name="departmentId"
                           value="<%= department.getDepartmentId() %>">
                    <% } %>

                    <div class="form-grid">

                        <!-- Department name -->
                        <div class="form-group full-width">

                            <label for="departmentName">
                                Department Name <span class="required">*</span>
                            </label>

                            <input type="text"
                                   id="departmentName"
                                   name="departmentName"
                                   placeholder="e.g. Human Resources"
                                   maxlength="100"
                                   value="<%= safeName %>"
                                   required>

                            <small class="department-field-hint">
                                Enter a unique name for this department.
                            </small>

                        </div>

                        <!-- Description -->
                        <div class="form-group full-width">

                            <label for="description">
                                Department Description
                            </label>

                            <textarea id="description"
                                      name="description"
                                      maxlength="255"
                                      placeholder="Describe the department's responsibilities and purpose..."><%= safeDescription %></textarea>

                            <small class="department-field-hint">
                                Briefly describe the department (maximum 255 characters).
                            </small>

                        </div>

                    </div>

                    <div class="form-actions">

                        <button type="submit" class="primary-button">
                            <%= isEdit ? "Save Changes" : "＋ Create Department" %>
                        </button>

                        <a href="<%= contextPath %>/departments"
                           class="secondary-button">
                            Cancel
                        </a>

                    </div>

                </form>

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

