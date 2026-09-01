<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RoxCode - Login</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="css/login.css">
</head>
<body>

<div class="bg-shape bg-shape-1"></div>
<div class="bg-shape bg-shape-2"></div>

<div class="login-card">

    <div class="app-brand">
        <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#38b69b" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            <rect x="3" y="3" width="7" height="7"></rect>
            <rect x="14" y="3" width="7" height="7"></rect>
            <rect x="14" y="14" width="7" height="7"></rect>
            <rect x="3" y="14" width="7" height="7"></rect>
        </svg>
        <span class="app-name">RoxCode</span>
    </div>

    <div class="form-container">
        <h2 class="form-heading">Login</h2>

        <!-- FORM ACTION SET TO login -->
        <form action="login" method="POST" class="auth-form">

            <div class="pill-input-box">
                <div class="icon-bubble">
                    <i class="fa-solid fa-bullseye"></i>
                </div>
                <input type="text" name="email" placeholder="Email or phone number" required autocomplete="off">
            </div>

            <div class="pill-input-box">
                <div class="icon-bubble">
                    <i class="fa-solid fa-lock"></i>
                </div>
                <input type="password" name="password" placeholder="Password" required>
            </div>

            <div class="form-bottom-row">
                <!-- Point to .jsp -->
                <a href="forgot-password.jsp" class="forgot-link">
                    Forgot password?
                </a>

                <button type="submit" class="btn-teal-login">
                    Login
                </button>
            </div>

            <div class="register-footer">
                <span>Don't have an account?</span>
                <a href="register.jsp" class="register-link">Register</a>
            </div>
        </form>
    </div>

</div>

</body>
</html>