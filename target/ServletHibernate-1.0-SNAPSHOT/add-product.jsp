<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>User Management Application</title>
</head>
<body>
<center>
    <h1>User Management</h1>
    <h2>
        <a href="new">Add New Product</a>
        &nbsp;&nbsp;&nbsp;
        <a href="list">List All Product</a>

    </h2>
</center>
<div align="center">
    <c:if test="${product != null}">
    <form action="update" method="post">
        </c:if>
        <c:if test="${product == null}">
        <form action="insert" method="post">
            </c:if>
            <table border="1" cellpadding="5">
                <caption>
                    <h2>
                        <c:if test="${product != null}">
                            Edit product
                        </c:if>
                        <c:if test="${product == null}">
                            Add New product
                        </c:if>
                    </h2>
                </caption>
                <c:if test="${product != null}">
                    <input type="hidden" name="id" value="<c:out value='${product.getId()}' />" />
                </c:if>
                <tr>
                    <th>Product Name: </th>
                    <td>
                        <input type="text" name="name"
                               value="<c:out value='${product.getName()}' />"
                        />
                    </td>
                </tr>
                <tr>
                    <th>Brand : </th>
                    <td>
                        <input type="text" name="brand"
                               value="<c:out value='${product.getBrand()}' />"
                        />
                    </td>
                </tr>
                <tr>
                    <th>Made In: </th>
                    <td>
                        <input type="text" name="madeIn"
                               value="<c:out value='${product.getMadeIn()}' />"
                        />
                    </td>
                </tr>
                <tr>
                    <th>Price: </th>
                    <td>
                        <input type="text" name="price"
                               value="<c:out value='${product.getPrice()}' />"
                        />
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <input type="submit" value="Save" />
                    </td>
                </tr>
            </table>
        </form>
</div>
</body>
</html>