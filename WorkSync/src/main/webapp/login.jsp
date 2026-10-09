<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WorkSync | Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>

<div class="login-container">

    <div class="login-brand">
        <h1>Work<span>Sync</span></h1>
        <p>Workforce Management & Operations Platform</p>
    </div>

    <div class="login-card">
        <h2>Welcome Back</h2>
        <p class="subtitle">Sign in to your WorkSync account</p>

        <% if (request.getAttribute("error") != null) { %>
        <div class="error-message">
            <%= request.getAttribute("error") %>
        </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/login" method="post">

            <label for="username">Username</label>
            <input
                    type="text"
                    id="username"
                    name="username"
                    placeholder="Enter your username"
                    autocomplete="username"
                    required>

            <label for="password">Password</label>
            <input
                    type="password"
                    id="password"
                    name="password"
                    placeholder="Enter your password"
                    autocomplete="current-password"
                    required>

            <button type="submit">Sign In</button>

        </form>
    </div>

    <p class="footer-text">Secure access to your workplace</p>

</div>

</body>
</html>

