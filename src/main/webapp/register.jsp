<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>RoxCode - Register</title>

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <link rel="stylesheet" href="css/register.css">
</head>

<body>

<div class="bg-shape bg-shape-1"></div>
<div class="bg-shape bg-shape-2"></div>

<div class="register-card">

    <!-- HEADER -->
    <header class="card-header">

        <div class="brand">

            <svg width="24" height="24"
                 viewBox="0 0 24 24"
                 fill="none"
                 stroke="#7ac4b4"
                 stroke-width="2.5"
                 stroke-linecap="round"
                 stroke-linejoin="round">

                <rect x="3" y="3" width="7" height="7"></rect>
                <rect x="14" y="3" width="7" height="7"></rect>
                <rect x="14" y="14" width="7" height="7"></rect>
                <rect x="3" y="14" width="7" height="7"></rect>

            </svg>

            <span class="brand-name">RoxCode</span>

        </div>

    </header>


    <!-- BODY -->
    <div class="card-body">


        <!-- FORM SECTION -->
        <div class="form-pane">


            <!-- LOGIN / SIGN UP -->
            <div class="tab-switcher">

                <button type="button"
                        class="tab-btn inactive"
                        onclick="window.location.href='login.jsp'">
                    Login
                </button>

                <button type="button"
                        class="tab-btn active">
                    Sign up
                </button>

            </div>


            <!-- REGISTRATION FORM -->
            <form action="register"
                  method="post"
                  class="auth-form"
                  onsubmit="return validateRegistration()">


                <!-- FULL NAME -->
                <div class="pill-input-box" id="fullNameBox">

                    <div class="icon-bubble">
                        <i class="fa-regular fa-user"></i>
                    </div>

                    <input type="text"
                           name="fullName"
                           id="fullName"
                           placeholder="Full Name"
                           required
                           autocomplete="off"
                           oninput="validateFullName()">

                </div>

                <div id="fullNameError" class="field-error"></div>




                <!-- EMAIL -->
                <div class="pill-input-box">

                    <div class="icon-bubble">
                        <i class="fa-solid fa-bullseye"></i>
                    </div>

                    <input type="email"
                           name="email"
                           placeholder="Email address"
                           required
                           autocomplete="off">

                </div>


                <!-- PASSWORD -->
                <div class="pill-input-box">

                    <div class="icon-bubble">
                        <i class="fa-solid fa-lock"></i>
                    </div>

                    <input type="password"
                           id="password"
                           name="password"
                           placeholder="Password"
                           required>

                    <i class="fa-solid fa-eye password-eye"
                       onclick="togglePassword('password', this)">
                    </i>

                </div>


                <!-- CONFIRM PASSWORD -->
                <div class="pill-input-box">

                    <div class="icon-bubble">
                        <i class="fa-solid fa-lock"></i>
                    </div>

                    <input type="password"
                           id="confirmPassword"
                           placeholder="Confirm Password"
                           required>

                    <i class="fa-solid fa-eye password-eye"
                       onclick="togglePassword('confirmPassword', this)">
                    </i>

                </div>


                <!-- PASSWORD STRENGTH -->
                <div class="password-strength">
                    <span id="strengthText"></span>
                </div>


                <!-- ROLE -->
                <div class="pill-input-box select-box">

                    <div class="icon-bubble">
                        <i class="fa-solid fa-user-shield"></i>
                    </div>

                    <select name="role" required>

                        <option value="STUDENT">
                            Student Candidate
                        </option>

                        <option value="INSTRUCTOR">
                            Instructor / Admin
                        </option>

                    </select>

                </div>


                <!-- BOTTOM -->
                <div class="form-bottom-row">

                    <span class="login-prompt">
                        Already have an account?

                        <a href="login.jsp"
                           class="login-link">
                            Login
                        </a>
                    </span>

                    <button type="submit"
                            class="btn-teal-register">
                        Register
                    </button>

                </div>

            </form>

        </div>


        <!-- RIGHT SIDE DESIGN -->
        <div class="visual-pane">

            <div class="mint-arc arc-outer"></div>

            <div class="mint-arc arc-middle"></div>

            <div class="mint-arc arc-inner"></div>

        </div>

    </div>

</div>


<script>

    /* =========================================
       FULL NAME VALIDATION
       ========================================= */
    function validateFullName() {

        const fullName =
            document.getElementById("fullName");

        const fullNameBox =
            document.getElementById("fullNameBox");

        const fullNameError =
            document.getElementById("fullNameError");


        if (fullName.value !== "" &&
            !/^[A-Za-z ]+$/.test(fullName.value)) {

            fullNameBox.classList.add("invalid");

            fullNameError.textContent =
                "Only alphabets and spaces are allowed.";

            return false;

        } else {

            fullNameBox.classList.remove("invalid");

            fullNameError.textContent = "";

            return true;
        }
    }


    /* =========================================
       REGISTRATION VALIDATION
       ========================================= */

    function validateRegistration() {

        const fullName =
            document.getElementById("fullName");

        const password =
            document.getElementById("password");

        const confirmPassword =
            document.getElementById("confirmPassword");


        /* Full Name */

        if (!/^[A-Za-z ]+$/.test(fullName.value)) {

            validateFullName();

            fullName.focus();

            return false;
        }


        /* Password Match */

        if (password.value !== confirmPassword.value) {

            alert("Passwords do not match!");

            confirmPassword.focus();

            return false;
        }


        return true;
    }


</script>

</body>
</html>