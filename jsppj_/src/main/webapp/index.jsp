<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<a href="./">헬로월드로 이동하기</a>
<a href="/jsppj/First">서블릿 헬로 월드로 이동하기</a>

<a href="<%=request.getContextPath()%>/member/memberList.do">회원정보 리스트 가기</a>
<a href="<%=request.getContextPath()%>/member/memberInput.do">회원가입하기</a>
<a href="<%=request.getContextPath()%>/board/boardList.do">게시판 목록가기</a>
</body>
</html>