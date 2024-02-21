<%--
  Created by IntelliJ IDEA.
  User: 김재헌
  Date: 2024-02-21
  Time: 오전 11:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>${message}</h1>

    <h1><c:out value="${message}"></c:out></h1>
</body>
</html>
