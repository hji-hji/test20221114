<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="domain.*" %>    
<%
BoardVo bv = (BoardVo)request.getAttribute("bv");
%>    
    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
내용보기
<table  border=1 style="width:500px;">
<tr>
<td style="width:50px;">제목</td>
<td><%=bv.getSubject() %>  조회수<%=bv.getViewcnt() %></td>
</tr>
<tr>
<td>파일다운로드</td>
<td><a href="<%=request.getContextPath() %>/board/fileDownload.do?filename=<%=bv.getFilename() %>"><%=bv.getFilename() %></a></td>
</tr>
<tr>
<td>이미지</td>
<td><image src="<%=request.getContextPath()%>/image/<%=bv.getFilename() %>"  width="100px"></td>
</tr>


<tr>
<td style="height:200px;">내용</td>
<td><%=bv.getContents() %></td>
</tr>
<tr>
<td>작성자</td>
<td><%=bv.getWriter() %></td>
</tr>
<tr>
<td colspan=2 style="text-align:right;">
<button onclick="location.href='<%=request.getContextPath() %>/board/boardModify.do?bidx=<%=bv.getBidx()%>' ">수정</button>
<button onclick="location.href='<%=request.getContextPath() %>/board/boardDelete.do?bidx=<%=bv.getBidx()%>' ">삭제</button>
<button onclick="location.href='<%=request.getContextPath() %>/board/boardReply.do?bidx=<%=bv.getBidx()%>&originbidx=<%=bv.getOriginbidx()%>&depth=<%=bv.getDepth()%>&level_=<%=bv.getLevel_()%>' ">답변</button>
<button onclick="location.href='<%=request.getContextPath() %>/board/boardList.do' ">목록</button>
</td>
</tr>
</table>
</body>
</html>