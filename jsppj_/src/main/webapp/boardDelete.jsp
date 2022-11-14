<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String bidx  = (String)request.getAttribute("bidx");
%>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
삭제페이지입니다
<button onclick="location.href='<%=request.getContextPath()%>/board/boardDeleteAction.do?bidx=<%=bidx%>'">
삭제신청</button>
</body>
</html>