<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>User Management Application</title>
</head>
<body>
<center>
    <c:if test="${sessionScope.username != null}">
        <p>Logged user : <b><c:out value="${sessionScope.username}"/></b></p>
        <a href="logout">logout</a>
    </c:if>
    <h1>Product Management</h1>
    <h2>
        <a href="new">Add New Product</a>
        &nbsp;&nbsp;&nbsp;
        <a href="list">List All Products</a>

    </h2>
</center>
<div align="center">
    <table border="1" cellpadding="5">
        <caption><h2>List of Products</h2></caption>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Country</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="product" items="${listProduct}">
            <tr>
                <td><c:out value="${product.getId()}" /></td>
                <td><c:out value="${product.getName()}" /></td>
                <td><c:out value="${product.getBrand()}" /></td>
                <td><c:out value="${product.getMadeIn()}" /></td>
                <td><c:out value="${product.getPrice()}" /></td>
                <td>
                    <a href="edit?id=<c:out value='${product.getId()}' />">Edit</a>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="delete?id=<c:out value='${product.getId()}' />">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
