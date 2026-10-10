<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WorkSync | Employees</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/employees.css">


</head>
<body>

<div class="page-container">

    <header class="page-header">
        <div>
            <a href="${pageContext.request.contextPath}/dashboard" class="back-link">
                ← Back to Dashboard
            </a>

            <h1>Employee Management</h1>

            <p>Manage your workforce and employee records.</p>
        </div>

        <a href="${pageContext.request.contextPath}/employees/add"
           class="add-button">
            + Add Employee
        </a>
    </header>

    <section class="employee-panel">

        <div class="panel-heading">
            <div>
                <h2>Employee Directory</h2>
                <p>Employee records registered in WorkSync.</p>
            </div>

            <span class="count-badge">
            Total: <c:out value="${employees.size()}"/>
        </span>
        </div>

        <div class="table-wrapper">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Employee</th>
                    <th>Job Title</th>
                    <th>Phone</th>
                    <th>Status</th>
                    <th>Joining Date</th>
                </tr>
                </thead>

                <tbody>
                <c:choose>
                    <c:when test="${empty employees}">
                        <tr>
                            <td colspan="6" class="empty-state">
                                <div class="empty-icon">♙</div>
                                <h3>No employees found</h3>
                                <p>Employee records will appear here when available.</p>
                            </td>
                        </tr>
                    </c:when>

                    <c:otherwise>
                        <c:forEach var="employee" items="${employees}">
                            <tr>
                                <td>#<c:out value="${employee.employeeId}"/></td>

                                <td>
                                    <div class="employee-name">
                                    <span class="employee-avatar">
                                        <c:out value="${employee.firstName.substring(0,1)}"/>
                                    </span>

                                        <div>
                                            <strong>
                                                <c:out value="${employee.firstName}"/>
                                                <c:out value="${employee.lastName}"/>
                                            </strong>
                                            <small>Employee</small>
                                        </div>
                                    </div>
                                </td>

                                <td>
                                    <c:out value="${employee.jobTitle}"/>
                                </td>

                                <td>
                                    <c:out value="${employee.phone}"/>
                                </td>

                                <td>
                                <span class="status-badge">
                                    <c:out value="${employee.employmentStatus}"/>
                                </span>
                                </td>

                                <td>
                                    <c:out value="${employee.joiningDate}"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>

    </section>
</div>

</body>
</html>
