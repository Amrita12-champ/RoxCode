
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

<div class="sidebar">

    <h1>Work<span>Sync</span></h1>

    <nav>
        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </nav>

</div>

<main class="main-content">

    <header class="topbar">
        <div>
            <h2>Dashboard</h2>
            <p>Workforce Management & Operations</p>
        </div>

        <div class="user-info">
            <span>${sessionScope.username}</span>
            <span class="role">${sessionScope.role}</span>
        </div>
    </header>

    <section class="welcome-card">
        <p class="eyebrow">WORKSPACE OVERVIEW</p>

        <h1>Welcome back, ${sessionScope.username}!</h1>

        <p>
            Your WorkSync workspace is ready.
            Manage your workforce and monitor daily operations from here.
        </p>
    </section>

    <section class="stats-grid">

        <div class="stat-card">
            <p>Employees</p>
            <h2>--</h2>
            <span>Employee management coming next</span>
        </div>

        <div class="stat-card">
            <p>Attendance</p>
            <h2>--</h2>
            <span>Attendance tracking coming next</span>
        </div>

        <div class="stat-card">
            <p>Leave Requests</p>
            <h2>--</h2>
            <span>Leave management coming next</span>
        </div>

        <div class="stat-card">
            <p>Your Role</p>
            <h2>${sessionScope.role}</h2>
            <span>Access based on your account</span>
        </div>

    </section>

</main>

</body>
</html>

