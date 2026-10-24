<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>

<html>
<head>
    <title>Reset Password | WorkSync</title>

    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #0d0d14;
            color: #ffffff;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }

        .reset-card {
            width: 350px;
            padding: 30px;
            background: #161621;
            border: 1px solid #29293b;
            border-radius: 12px;
        }

        h2 {
            color: #a78bfa;
            text-align: center;
        }

        label {
            display: block;
            margin-top: 16px;
            margin-bottom: 7px;
        }

        input {
            box-sizing: border-box;
            width: 100%;
            padding: 12px;
            background: #0d0d14;
            border: 1px solid #343449;
            border-radius: 6px;
            color: white;
        }

        button {
            width: 100%;
            margin-top: 22px;
            padding: 12px;
            border: none;
            border-radius: 6px;
            background: #8b5cf6;
            color: white;
            font-weight: bold;
            cursor: pointer;
        }

        .error {
            color: #f87171;
        }

        .success {
            color: #4ade80;
        }

        a {
            display: block;
            margin-top: 18px;
            text-align: center;
            color: #a78bfa;
            text-decoration: none;
        }
    </style>

</head>

<body>

<div class="reset-card">

    <h2>Reset Password</h2>

    <p class="error">
        <%= request.getAttribute("error") == null
                ? "" : request.getAttribute("error") %>
    </p>

    <p class="success">
        <%= request.getAttribute("success") == null
                ? "" : request.getAttribute("success") %>
    </p>

    <form action="${pageContext.request.contextPath}/reset-password"
          method="post">

        <label>Employee Username</label>
        <input type="text" name="username" required>

        <label>New Password</label>
        <input type="password" name="newPassword"
               minlength="8" required>

        <label>Confirm New Password</label>
        <input type="password" name="confirmPassword"
               minlength="8" required>

        <button type="submit">Update Password</button>

    </form>

    <a href="${pageContext.request.contextPath}/login">
        Back to Login
    </a>


</div>

</body>
</html>
