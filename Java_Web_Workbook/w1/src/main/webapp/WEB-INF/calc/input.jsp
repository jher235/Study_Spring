<%--
  Created by IntelliJ IDEA.
  User: 김재헌
  Date: 2024-02-14
  Time: 오후 6:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<form action="/calc/makeResult" method="post">    <%--action과 method 추가 calcResult.jsp으로 전송함--%>
    <input type="number" name="num1">
    <input type="number" name="num2">
    <button type="submit">SEND</button>
</form>

</body>
</html>
