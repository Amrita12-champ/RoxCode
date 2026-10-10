<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>WorkSync | Edit Employee</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/employees.css">

    <style>
        .edit-container {
            max-width: 900px;
            margin: 40px auto;
            padding: 0 24px 40px;
        }

        .edit-card {
            background: #ffffff;
            border-radius: 16px;
            padding: 32px;
            box-shadow: 0 8px 30px rgba(0, 0, 0, 0.06);
        }

        .edit-card h2 {
            margin-top: 0;
            margin-bottom: 8px;
        }

        .edit-description {
            color: #6b7280;
            margin-bottom: 28px;
        }

        .form-grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 22px;
        }

        .form-group {
            display: flex;
            flex-direction: column;
            gap: 8px;
        }

        .form-group label {
            font-size: 14px;
            font-weight: 600;
            color: #374151;
        }

        .form-group input,
        .form-group select {
            width: 100%;
            box-sizing: border-box;
            padding: 12px 14px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 14px;
            background: #ffffff;
            color: #111827;
        }

        .form-group input:focus,
        .form-group select:focus {
            outline: none;
            border-color: #4f46e5;
            box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.12);
        }

        .form-actions {
            display: flex;
            justify-content: flex-end;
            gap: 12px;
            margin-top: 30px;
        }

        .save-button,
        .cancel-button {
            display: inline-block;
            padding: 12px 22px;
            border-radius: 8px;
            text-decoration: none;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            border: none;
        }

        .save-button {
            background: #4f46e5;
            color: #ffffff;
        }

        .save-button:hover {
            background: #4338ca;
        }

        .cancel-button {
            background: #f3f4f6;
            color: #374151;
        }

        .cancel-button:hover {
            background: #e5e7eb;
        }

        .error-message {
            background: #fef2f2;
            color: #b91c1c;
            padding: 12px 16px;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .required {
            color: #dc2626;
        }

        @media (max-width: 650px) {
            .form-grid {
                grid-template-columns: 1fr;
            }

            .edit-card {
                padding: 22px;
            }
        }
    </style>
</head>

<body>

<div class="edit-container">

    <header class="page-header">
        <div>
            <a href="${pageContext.request.contextPath}/employees"
               class="back-link">
                ← Back to Employees
            </a>

            <h1>Edit Employee</h1>

            <p>Update employee information and work details.</p>
        </div>
    </header>

    <section class="edit-card">

        <h2>Employee Details</h2>

        <p class="edit-description">
            Editing employee #<c:out value="${employee.employeeId}"/>
        </p>

        <c:if test="${not empty error}">
            <div class="error-message">
                <c:out value="${error}"/>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/employees"
              method="post">

            <input type="hidden"
                   name="action"
                   value="update">

            <input type="hidden"
                   name="employeeId"
                   value="<c:out value='${employee.employeeId}'/>">

            <div class="form-grid">

                <div class="form-group">
                    <label for="firstName">
                        First Name <span class="required">*</span>
                    </label>

                    <input type="text"
                           id="firstName"
                           name="firstName"
                           value="<c:out value='${employee.firstName}'/>"
                           maxlength="50"
                           required>
                </div>

                <div class="form-group">
                    <label for="lastName">
                        Last Name <span class="required">*</span>
                    </label>

                    <input type="text"
                           id="lastName"
                           name="lastName"
                           value="<c:out value='${employee.lastName}'/>"
                           maxlength="50"
                           required>
                </div>

                <div class="form-group">
                    <label for="phone">Phone</label>

                    <input type="tel"
                           id="phone"
                           name="phone"
                           value="<c:out value='${employee.phone}'/>"
                           maxlength="15">
                </div>

                <div class="form-group">
                    <label for="jobTitle">Job Title</label>

                    <input type="text"
                           id="jobTitle"
                           name="jobTitle"
                           value="<c:out value='${employee.jobTitle}'/>"
                           maxlength="100">
                </div>

                <div class="form-group">
                    <label for="departmentId">Department</label>

                    <select id="departmentId" name="departmentId">

                        <option value="">No Department</option>

                        <c:forEach var="department" items="${departments}">

                            <option value="${department.departmentId}"
                                    <c:if test="${not empty employee.department and employee.department.departmentId eq department.departmentId}">
                                        selected
                                    </c:if>>

                                <c:out value="${department.departmentName}"/>

                            </option>

                        </c:forEach>

                    </select>
                </div>

                <div class="form-group">
                    <label for="managerId">Manager</label>

                    <select id="managerId" name="managerId">

                        <option value="">No Manager</option>

                        <c:forEach var="manager" items="${managers}">

                            <c:if test="${manager.employeeId ne employee.employeeId
                                          and manager.employmentStatus ne 'RESIGNED'
                                          and manager.employmentStatus ne 'INACTIVE'}">

                                <option value="${manager.employeeId}"
                                        <c:if test="${not empty employee.manager and employee.manager.employeeId eq manager.employeeId}">
                                            selected
                                        </c:if>>

                                    <c:out value="${manager.firstName}"/>
                                    <c:out value="${manager.lastName}"/>

                                </option>

                            </c:if>

                        </c:forEach>

                    </select>
                </div>

                <div class="form-group">
                    <label for="employmentStatus">Employment Status</label>

                    <select id="employmentStatus" name="employmentStatus">

                        <option value="ACTIVE"
                                <c:if test="${employee.employmentStatus eq 'ACTIVE'}">
                                    selected
                                </c:if>>
                            Active
                        </option>

                        <option value="INACTIVE"
                                <c:if test="${employee.employmentStatus eq 'INACTIVE'}">
                                    selected
                                </c:if>>
                            Inactive
                        </option>

                        <option value="ON_LEAVE"
                                <c:if test="${employee.employmentStatus eq 'ON_LEAVE'}">
                                    selected
                                </c:if>>
                            On Leave
                        </option>

                        <option value="RESIGNED"
                                <c:if test="${employee.employmentStatus eq 'RESIGNED'}">
                                    selected
                                </c:if>>
                            Resigned
                        </option>

                    </select>
                </div>

                <div class="form-group">
                    <label for="joiningDate">Joining Date</label>

                    <input type="date"
                           id="joiningDate"
                           name="joiningDate"
                           value="<c:out value='${employee.joiningDate}'/>">
                </div>

            </div>

            <div class="form-actions">

                <a href="${pageContext.request.contextPath}/employees"
                   class="cancel-button">
                    Cancel
                </a>

                <button type="submit" class="save-button">
                    Save Changes
                </button>

            </div>

        </form>

    </section>

</div>

</body>
</html>