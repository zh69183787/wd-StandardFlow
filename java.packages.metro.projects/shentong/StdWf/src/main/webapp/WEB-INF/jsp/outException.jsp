<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>异常处理页面</title>
</head>
<body>
	<% Exception ex = (Exception) request.getAttribute("exception");%>
	<h5>系统出错，请联系管理员。</h5>
	<h5>错误:<%=ex.getMessage() %></h5>
</body>
</html>