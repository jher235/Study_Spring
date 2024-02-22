<%--
  Created by IntelliJ IDEA.
  User: 김재헌
  Date: 2024-02-22
  Time: 오후 7:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Hello World!</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
    <div class="card">
        <h5 class="card-header">Featured</h5>
        <div class="card-body">
           <h5 class="card-title">Special title treatment</h5>
            <table class="table">
                <thread>
                    <tr>
                        <th scope="col">Tno</th>
                        <th scope="col">Title</th>
                        <th scope="col">Writer</th>
                        <th scope="col">DueDate</th>
                        <th scope="col">Finished</th>
                    </tr>
                </thread>
                <tbody>
                    <c:forEach items="${dtoList}" var="dto">
                        <tr>
                            <th scope="row"><c:out value="${dto.tno}"/> </th>
                            <td><a href="/todo/read?tno=${dto.tno}" class="text-decoration-none"><c:out value="${dto.title}"/></a> </td>
                            <td><c:out value="${dto.writer}"/> </td>
                            <td><c:out value="${dto.dueDate}"/> </td>
                            <td><c:out value="${dto.finished}"/> </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
