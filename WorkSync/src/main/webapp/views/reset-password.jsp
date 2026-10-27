<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password | WorkSync</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #0d0d14;
            color: #f4f3fb;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .reset-card {
            width: 100%;
            max-width: 410px;
            padding: 32px;
            background: #161621;
            border: 1px solid #29293a;
            border-radius: 14px;
        }

        .brand {
            text-align: center;
            margin-bottom: 25px;
        }

        .brand h1 {
            margin: 0;
            font-size: 30px;
            color: #f4f3fb;
        }

        .brand span {
            color: #a78bfa;
        }

        h2 {
            margin-bottom: 8px;
            text-align: center;
        }

        .subtitle {
            text-align: center;
            color: #9695ad;
            font-size: 14px;
            margin-bottom: 25px;
        }

        label {
            display: block;
            margin: 17px 0 8px;
            font-size: 14px;
        }

        input {
            width: 100%;
            padding: 12px;
            color: #f4f3fb;
            background: #0d0d14;
            border: 1px solid #29293a;
            border-radius: 7px;
            outline: none;
        }

        input:focus {
            border-color: #a78bfa;
        }

        button {
            width: 100%;
            margin-top: 24px;
            padding: 13px;
            background: #8b5cf6;
            border: none;
            border-radius: 7px;
            color: white;
            font-size: 15px;
            font-weight: bold;
            cursor: pointer;
        }

        button:hover {
            background: #7c3aed;
        }

        .message {
            margin: 12px 0;
            text-align: center;
            font-size: 14px;
        }

        .error {
            color: #f87171;
        }

        .success {
            color: #4ade80;
        }

        .back-link {
            display: block;
            margin-top: 20px;
            text-align: center;
            color: #a78bfa;
            text-decoration: none;
            font-size: 14px;
        }

        .back-link:hover {
            text-decoration: underline;
        }
    </style>

</head>

<body>

<div class="reset-card">

    <div class="brand">
        <h1>Work<span>Sync</span></h1>
    </div>

    <h2>Reset Password</h2>
    <p class="subtitle">Choose a new password for your employee account.</p>

    <% if (request.getAttribute("error") != null) { %>
    <div class="message error">
        <%= request.getAttribute("error") %>
    </div>
    <% } %>

    <% if (request.getAttribute("success") != null) { %>
    <div class="message success">
        <%= request.getAttribute("success") %>
    </div>
    <% } %>

    <form action="<%= request.getContextPath() %>/reset-password"
          method="post">

        <label for="username">Employee Username</label>
        <input type="text"
               id="username"
               name="username"
               autocomplete="username"
               required>

        <label for="newPassword">New Password</label>
        <input type="password"
               id="newPassword"
               name="newPassword"
               minlength="8"
               autocomplete="new-password"
               required>

        <label for="confirmPassword">Confirm New Password</label>
        <input type="password"
               id="confirmPassword"
               name="confirmPassword"
               minlength="8"
               autocomplete="new-password"
               required>

        <button type="submit">Update Password</button>

    </form>

    <a class="back-link"
       href="${pageContext.request.contextPath}/login">
        Back to Login
    </a>

</div>

</body>
</html>
