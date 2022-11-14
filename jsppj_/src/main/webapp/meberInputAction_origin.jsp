<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import ="java.sql.*" %>    
 <% 
 request.setCharacterEncoding("UTF-8");
 response.setContentType("text/html;charset=UTF-8");
 
 String memberId = request.getParameter("memberId");
 String memberPwd = request.getParameter("memberPwd");
 String memberPwd2 = request.getParameter("memberPwd2");
 String memberName = request.getParameter("memberName");
 String memberBirth = request.getParameter("memberBirth");
 String memberGender = request.getParameter("memberGender");
 String memberArea = request.getParameter("memberArea");
 
 out.println("memberId는 ?"+memberId);
 out.println("memberPwd는 ?"+memberPwd);
 out.println("memberName는 ?"+memberName);
 out.println("memberBirth는 ?"+memberBirth);
 out.println("memberGender는 ?"+memberGender);
 out.println("memberArea는 ?"+memberArea);
 
 String url = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
 String user="system";
 String password="1234";
 
 Class.forName("oracle.jdbc.driver.OracleDriver");
 Connection conn = DriverManager.getConnection(url, user, password);
 out.println("conn:"+conn);
 
 String sql = "insert into member0817(midx,memberId,memberPwd,memberName,"
		 		   +"memberBirth,memberGender,memberArea) "
        		   +"values(midx_seq.nextval, '"+memberId+"','"+memberPwd+"','"+memberName+"','"
		 		   +memberBirth+"','"+memberGender+"','"+memberArea+"' )";
Statement stmt = conn.createStatement();
 stmt.execute(sql);
 
conn.close();
 
 
 
 %>       
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
넘겨받은 페이지입니다.
</body>
</html>