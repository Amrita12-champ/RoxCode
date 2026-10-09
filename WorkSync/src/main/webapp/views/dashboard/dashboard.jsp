<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WorkSync | Dashboard</title>
    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/dashboard.css">


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
            <a class="nav-link active"
               href="<%= request.getContextPath() %>/dashboard">
                <span class="nav-icon">▦</span>
                Dashboard
            </a>

            <a class="nav-link"
               href="<%= request.getContextPath() %>/employees">
                <span class="nav-icon">♙</span>
                Employees
            </a>

            <a class="nav-link"
               href="<%= request.getContextPath() %>/departments">
                <span class="nav-icon">▤</span>
                Departments
            </a>

            <a class="nav-link"
               href="<%= request.getContextPath() %>/attendance">
                <span class="nav-icon">◷</span>
                Attendance
            </a>

            <a class="nav-link"
               href="<%= request.getContextPath() %>/leaves">
                <span class="nav-icon">▧</span>
                Leave Management
            </a>
        </nav>

        <div class="sidebar-bottom">
            <div class="help-card">
                <div class="help-icon">✦</div>
                <h3>WorkSync Workspace</h3>
                <p>Your workforce, organized in one place.</p>
            </div>

            <a class="nav-link logout-link"
               href="<%= request.getContextPath() %>/logout">
                <span class="nav-icon">↪</span>
                Logout
            </a>
        </div>
    </aside>

    <main class="main-content">

        <header class="topbar">
            <div class="page-heading">
                <p class="breadcrumb">WORKSPACE / OVERVIEW</p>
                <h2>Dashboard</h2>
                <p class="page-subtitle">
                    Here's what's happening in your workspace.
                </p>
            </div>

            <div class="user-info">
                <div class="user-avatar">
                    <%= session.getAttribute("username") != null
                            ? session.getAttribute("username").toString().substring(0, 1).toUpperCase()
                            : "U" %>
                </div>

                <div class="user-details">
                    <strong><%= session.getAttribute("username") %></strong>
                    <span><%= session.getAttribute("role") %></span>
                </div>
            </div>
        </header>

        <section class="welcome-card">
            <div class="welcome-content">
                <span class="welcome-label">YOUR WORKSPACE</span>

                <h1>Welcome back, <%= session.getAttribute("username") %>!</h1>
                <p>
                    Manage your team, monitor daily operations, and keep
                    everything running smoothly from one place.
                </p>

                <a class="primary-button"
                   href="<%= request.getContextPath() %>/employees">
                    Explore Employees <span>→</span>
                </a>
            </div>

            <div class="welcome-decoration">
                <div class="decoration-circle circle-one"></div>
                <div class="decoration-circle circle-two"></div>
                <div class="decoration-center">W</div>
            </div>
        </section>

        <section class="section-heading">
            <div>
                <h2>Workspace Overview</h2>
                <p>A quick look at your workforce operations.</p>
            </div>

            <span class="status-badge">
            <span class="status-dot"></span>
            Workspace Active
        </span>
        </section>

        <section class="stats-grid">

            <article class="stat-card">
                <div class="stat-top">
                    <div class="stat-icon employees-icon">♙</div>
                    <span class="stat-tag">PEOPLE</span>
                </div>

                <p class="stat-label">Total Employees</p>
                <h3>--</h3>
                <p class="stat-description">Registered workforce members</p>
            </article>

            <article class="stat-card">
                <div class="stat-top">
                    <div class="stat-icon attendance-icon">◷</div>
                    <span class="stat-tag">TODAY</span>
                </div>

                <p class="stat-label">Present Today</p>
                <h3>--</h3>
                <p class="stat-description">Attendance summary</p>
            </article>

            <article class="stat-card">
                <div class="stat-top">
                    <div class="stat-icon leave-icon">▧</div>
                    <span class="stat-tag">REQUESTS</span>
                </div>

                <p class="stat-label">Pending Leave Requests</p>
                <h3>--</h3>
                <p class="stat-description">Awaiting manager review</p>
            </article>

            <article class="stat-card">
                <div class="stat-top">
                    <div class="stat-icon department-icon">▤</div>
                    <span class="stat-tag">TEAMS</span>
                </div>

                <p class="stat-label">Departments</p>
                <h3>--</h3>
                <p class="stat-description">Organizational departments</p>
            </article>

        </section>

        <section class="bottom-grid">

            <article class="content-card">
                <div class="card-heading">
                    <div>
                        <h3>Quick Actions</h3>
                        <p>Shortcuts to your workspace modules.</p>
                    </div>
                </div>

                <div class="quick-actions">
                    <a class="action-item"
                       href="<%= request.getContextPath() %>/employees">
                        <span class="action-icon">♙</span>
                        <span>
                        <strong>Employees</strong>
                        <small>Manage employee records</small>
                    </span>
                        <span class="action-arrow">→</span>
                    </a>

                    <a class="action-item"
                       href="<%= request.getContextPath() %>/departments">
                        <span class="action-icon">▤</span>
                        <span>
                        <strong>Departments</strong>
                        <small>Organize your teams</small>
                    </span>
                        <span class="action-arrow">→</span>
                    </a>

                    <a class="action-item"
                       href="<%= request.getContextPath() %>/attendance">
                        <span class="action-icon">◷</span>
                        <span>
                        <strong>Attendance</strong>
                        <small>Review attendance records</small>
                    </span>
                        <span class="action-arrow">→</span>
                    </a>
                </div>
            </article>

            <article class="content-card activity-card">
                <div class="card-heading">
                    <div>
                        <h3>Workspace Activity</h3>
                        <p>Your workspace at a glance.</p>
                    </div>
                    <span class="activity-indicator"></span>
                </div>

                <div class="activity-item">
                    <span class="activity-marker"></span>
                    <div>
                        <strong>Account authenticated</strong>
                        <p>You successfully signed in to WorkSync.</p>
                    </div>
                </div>

                <div class="activity-item">
                    <span class="activity-marker muted-marker"></span>
                    <div>
                        <strong>Employee management</strong>
                        <p>Employee records will appear here as the module is built.</p>
                    </div>
                </div>

                <div class="activity-item">
                    <span class="activity-marker muted-marker"></span>
                    <div>
                        <strong>Attendance and leave</strong>
                        <p>Operational summaries will be added in upcoming modules.</p>
                    </div>
                </div>
            </article>

        </section>

        <footer class="dashboard-footer">
            <span>© 2026 WorkSync</span>
            <span>Workforce Management &amp; Operations Platform</span>
        </footer>

    </main>


</div>

</body>
</html>
