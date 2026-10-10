<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WorkSync | Add Employee</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/employees.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/add-employee.css">
</head>
<body>

<div class="form-page">

    <header class="form-page-header">
        <div>
            <a href="${pageContext.request.contextPath}/employees" class="back-link">
                ← Back to Employees
            </a>
            <h1>Add New Employee</h1>
            <p>Create an employee profile and login account.</p>
        </div>
    </header>

    <c:if test="${not empty error}">
        <div class="form-alert">
            <c:out value="${error}"/>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/employees/add"
          method="post" class="employee-form">

        <section class="form-section">
            <h2>Personal Information</h2>
            <p>Enter the employee's basic details.</p>

            <div class="form-grid">
                <div class="field">
                    <label for="firstName">First Name *</label>
                    <input id="firstName" name="firstName" type="text"
                           maxlength="50" required>
                </div>

                <div class="field">
                    <label for="lastName">Last Name *</label>
                    <input id="lastName" name="lastName" type="text"
                           maxlength="50" required>
                </div>

                <div class="field">
                    <label for="email">Email *</label>
                    <input id="email" name="email" type="email"
                           maxlength="100" required>
                </div>

                <div class="field">
                    <label for="phone">Phone</label>
                    <input id="phone" name="phone" type="tel"
                           maxlength="15">
                </div>
            </div>
        </section>

        <section class="form-section">
            <h2>Account Information</h2>
            <p>These credentials will be used to sign in.</p>

            <div class="form-grid">
                <div class="field">
                    <label for="username">Username *</label>
                    <input id="username" name="username" type="text"
                           maxlength="50" autocomplete="off" required>
                </div>

                <div class="field">
                    <label for="password">Initial Password *</label>
                    <input id="password" name="password" type="password"
                           minlength="8" autocomplete="new-password" required>
                </div>

                <div class="field">
                    <label for="role">Account Role *</label>
                    <select id="role" name="role" required>
                        <option value="EMPLOYEE">Employee</option>
                        <option value="DEPARTMENT_MANAGER">Department Manager</option>
                        <option value="HR_MANAGER">HR Manager</option>
                        <option value="AUDITOR">Auditor</option>
                    </select>
                </div>

                <div class="field">
                    <label for="status">Employment Status *</label>
                    <select id="status" name="status" required>
                        <option value="ACTIVE">Active</option>
                        <option value="INACTIVE">Inactive</option>
                        <option value="ON_LEAVE">On Leave</option>
                        <option value="RESIGNED">Resigned</option>
                    </select>
                </div>
            </div>
        </section>

        <section class="form-section">
            <h2>Work Information</h2>
            <p>Assign the employee to a department and role.</p>

            <div class="form-grid">
                <div class="field">
                    <label for="jobTitle">Job Title *</label>
                    <input id="jobTitle" name="jobTitle" type="text"
                           maxlength="100" required>
                </div>

                <div class="field">
                    <label for="departmentId">Department</label>
                    <select id="departmentId" name="departmentId">
                        <option value="">No department</option>
                        <c:forEach var="department" items="${departments}">
                            <option value="${department.id}">
                                <c:out value="${department.name}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="field">
                    <label for="managerId">Reporting Manager</label>
                    <select id="managerId" name="managerId">
                        <option value="">No manager</option>
                        <c:forEach var="manager" items="${managers}">
                            <option value="${manager.id}">
                                <c:out value="${manager.name}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="field">
                    <label for="joiningDate">Joining Date *</label>
                    <input id="joiningDate" name="joiningDate"
                           type="date" required>
                </div>
            </div>
        </section>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/employees"
               class="cancel-button">Cancel</a>

            <button type="submit" class="save-button">Create Employee</button>
        </div>

    </form>


</div>

</body>
</html>
