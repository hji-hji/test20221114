package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/FrontController")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		//한글깨짐방지
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String uri = request.getRequestURI();   // 전체 가상주소 경로를 받는다
	    String pjName = 	request.getContextPath(); //프로젝트 이름경로를 추출한다
		int jari = pjName.length();			//프로젝트 이름 자리수를 추출한다
		String str = uri.substring(jari);		//프로젝트 이름을 제왼한 가상주소를 추출한다
		
		//예)   /board/boardWrite.do
		System.out.println("FrontController 가상경로 주소는?"+str);
		
		String[] fileObj = str.split("/");
		if (fileObj[1].equals("board")) {
			BoardController bc = new BoardController();
			bc.doGet(request, response);			
			
		}else if (fileObj[1].equals("member")) {
			MemberController mc = new MemberController();
			mc.doGet(request, response);
			
		}		
		
	}	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doGet(request, response);
	}

}
