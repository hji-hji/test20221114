<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
 <%@page import ="service.MemberDao" %>   
 <%@page import ="java.util.*" %>
 <%@page import ="domain.MemberVo" %>
 <%
 MemberDao md = new MemberDao();
 ArrayList<MemberVo> alist = md.memberList();

 %>
      
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
회원목록
<table border=1>
<tr>
<td>회원번호</td>
<td>회원아이디</td>
<td>회원이름</td>
<td>회원생년월일</td>
<td>회원성별</td>
<td>회원지역</td>
</tr>
<% for (MemberVo mv : alist) {%>
	<tr>
	<td><%=mv.getMidx() %></td>
	<td><%=mv.getMemberId() %></td>
	<td><%=mv.getMemberName() %></td>
	<td><%=mv.getMemberBirth() %></td>
	<td><%=mv.getMemberGender() %></td>
	<td><%=mv.getMemberArea() %></td>
	</tr>
<%} %>
</table>

</body>
</html>