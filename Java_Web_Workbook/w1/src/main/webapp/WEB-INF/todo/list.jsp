<%--
  Created by IntelliJ IDEA.
  User: 김재헌
  Date: 2024-02-15
  Time: 오후 12:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--jstl사용하기 위해 지시자 선언--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>List Page</h1>

<%--var=변수명, value=값 --%>
<c:set var="target" value="5"></c:set>

<ul>
<%--  var:EL문에서 사용될 변수 이름, items:list, set, map등의 컬렉션 --%>
    <c:forEach var="num" begin="1" end="10" items="${list}">
<%--    <c:forEach var="num" items="${list}">--%>
        <c:if test="${num == target}">
            target
        </c:if>
        <li>${num}</li>
    </c:forEach>

<%--    <c:if test="${list.size() %2==0}">--%>
<%--        짝수--%>
<%--    </c:if>--%>

<%--    <c:if test="${list.size() %2!=0}">--%>
<%--        홀수--%>
<%--    </c:if>--%>
    <c:choose>
        <c:when test="${list.size()%2==0}">짝수</c:when>
        <c:otherwise>홀수</c:otherwise>
    </c:choose>

</ul>
<%--${list}--%>


</body>
</html>
