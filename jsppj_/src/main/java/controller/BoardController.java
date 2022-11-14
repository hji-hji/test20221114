package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import domain.BoardVo;
import domain.Criteria;
import domain.PageMaker;
import domain.SearchCriteria;
import service.BoardDao;
import service.MemberDao;

@WebServlet("/BoardController")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
     	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("넘어오나요?");
		
		//한글깨짐방지
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
	//	String filePath ="D:\\dev0817\\eclipse-workspace\\uploadFiles\\";
		String filePath = "D:\\dev0817\\eclipse-workspace\\jsppj_\\src\\main\\webapp\\image\\";
		
		
		int fileSize = 15*1024*1024;  //15M
		String fileEncoding ="UTF-8";
		
		String uri = request.getRequestURI();   // 전체 가상주소 경로를 받는다
	    String pjName = 	request.getContextPath(); //프로젝트 이름경로를 추출한다
		int jari = pjName.length();			//프로젝트 이름 자리수를 추출한다
		String str = uri.substring(jari);		//프로젝트 이름을 제왼한 가상주소를 추출한다
		
		System.out.println("board 가상경로 주소는?"+str);
				
		if(str.equals("/board/boardList.do")) {
			//  /board/boardList.do 이 가상경로로 들어오면
			//  포워드 방식으로 boardList.jsp를 나타내준다
			
			//1.페이지 번호를 넘겨준다
			String page = request.getParameter("page");
			if (page == null) { 
				page = "1";
			}					
			String searchType = request.getParameter("searchType");  //검색기능 선택창
			if (searchType == null) {
				searchType="subject";
			}
			String keyword = request.getParameter("keyword");  //검색기능 키워드입력창
			if (keyword ==null) {
				keyword = "";
			}			
				//페이징의 기준이 되는 객체를 생성하여 설정값을 정의한다
				SearchCriteria scri = new SearchCriteria();
				scri.setPage( Integer.parseInt(page) );	 // 넘어온 현재 페이지번호를 담는다
				scri.setSearchType(searchType);
				scri.setKeyword(keyword);				
				
				BoardDao bd = new BoardDao();  
				ArrayList<BoardVo> alist = bd.boardList(scri);   //cri 객체를 매개변수로 넘긴다
				int cnt = bd.boardTotal(scri);  // 전체 게시물 수를 구한다
				
				//네이게이션에 필요한 설정값을 담는 객체를 생성하여  cri객체과 전체개수를 담는다
				PageMaker pm = new PageMaker();
				pm.setSearchCri(scri);
				pm.setTotalCount(cnt);				
				
				request.setAttribute("alist", alist);
				request.setAttribute("pm", pm);
				
			String path = "/boardList.jsp";
			RequestDispatcher rd =request.getRequestDispatcher(path);
			rd.forward(request, response);
		}else if (str.equals("/board/boardWrite.do")) {
				
			String path = "/boardWrite.jsp";
			RequestDispatcher rd =request.getRequestDispatcher(path);
			rd.forward(request, response);
			
		}else if (str.equals("/board/boardWriteAction.do")) {			
			
			DefaultFileRenamePolicy reNamePolicy = new DefaultFileRenamePolicy();  //파일이름중복정책
			
			MultipartRequest multiRequest = 
					new MultipartRequest(request,filePath,fileSize,fileEncoding,reNamePolicy);
						
			//입력값을 넘겨준다
			String subject = multiRequest.getParameter("subject");
			String contents = multiRequest.getParameter("contents");
			String writer = multiRequest.getParameter("writer");
			
			//열거자에 저장될 파일이름을 담는다
			Enumeration files =   multiRequest.getFileNames();
			//열거자에 담긴 각 파일을 옮겨 담는다
			String file = (String)files.nextElement();
			//저장되는 이름을 추출
			String fileName = multiRequest.getFilesystemName(file);
			//원래 파일 이름 추출
			String originFileName= multiRequest.getOriginalFileName(file);
								
			//ip를 추출한다
			String ip = 	InetAddress.getLocalHost().getHostAddress();
			
			int midx = 1; 
			
			BoardDao bd = new BoardDao();
			int value = bd.boardInsert(subject, contents, writer, ip, midx, fileName);
						
			if (value==1) {  //입력성공
			String path =request.getContextPath()+"/board/boardList.do";   //send로이동시 외부경로 주소씀
			response.sendRedirect(path);
			}else {  //입력실패
			String path =request.getContextPath()+"/board/boardWrite.do";   //send로이동시 외부경로 주소씀
			response.sendRedirect(path);	
			}
		}else if (str.equals("/board/boardContents.do")) {
			
			String bidx = request.getParameter("bidx");
			
			BoardDao bd = new BoardDao();
			BoardVo bv= bd.boardSelectOne( Integer.parseInt(bidx) );
			
			request.setAttribute("bv", bv);
			
		String path = "/boardContents.jsp";
		RequestDispatcher rd =request.getRequestDispatcher(path);
		rd.forward(request, response);
		
		}else if (str.equals("/board/boardModify.do")) {
			
			String bidx = request.getParameter("bidx");
			
			BoardDao bd = new BoardDao();
			BoardVo bv= bd.boardSelectOne( Integer.parseInt(bidx) );
			
			request.setAttribute("bv", bv);				
			
			String path = "/boardModify.jsp";
			RequestDispatcher rd =request.getRequestDispatcher(path);
			rd.forward(request, response);
		}else if (str.equals("/board/boardModifyAction.do")) {
		
			String bidx = request.getParameter("bidx");
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			
			BoardDao bd = new BoardDao();
			int value = bd.boardUpdate(subject, contents, writer, Integer.parseInt(bidx) );
			
			if (value ==1) {  //수정성공시
				String path =request.getContextPath()+"/board/boardContents.do?bidx="+bidx;   //send로이동시 외부경로 주소씀
				response.sendRedirect(path);
			}else { //수정 실패시
				String path =request.getContextPath()+"/board/boardModify.do?bidx="+bidx;   //send로이동시 외부경로 주소씀
				response.sendRedirect(path);	
			}
		}else if (str.equals("/board/boardDelete.do")) {
			//가상경로로 넘어옴
			String bidx =  request.getParameter("bidx");
			
			//request객체에  bidx이름으로 bidx값을 담아서  jsp화면에서 공유한다
			 request.setAttribute("bidx", bidx);
			//내부이동 공유
			//삭제화면을 보여줌
			String path = "/boardDelete.jsp";
			RequestDispatcher  rd = request.getRequestDispatcher(path);
			rd.forward(request, response);			
		}else if (str.equals("/board/boardDeleteAction.do")) {
			String bidx = request.getParameter("bidx");
			int bidx2 = Integer.parseInt(bidx);			
			
			BoardDao bd = new BoardDao();
			int value = bd.boardDelete(bidx2);			
			
			if (value ==1) {  //삭제성공시
				String path =request.getContextPath()+"/board/boardList.do";   //send로이동시 외부경로 주소씀
				response.sendRedirect(path);
			}else { //삭제 실패시
				String path =request.getContextPath()+"/board/boardDelete.do?bidx="+bidx;   //send로이동시 외부경로 주소씀
				response.sendRedirect(path);	
			}
			
		}else if (str.equals("/board/boardReply.do")) {
			
			String bidx = request.getParameter("bidx");
			int bidx2 = Integer.parseInt(bidx);   // 숫자형 변환			
			String originbidx = request.getParameter("originbidx");
			int originbidx2 = Integer.parseInt(originbidx);			
			String depth = request.getParameter("depth");
			int depth2 = Integer.parseInt(depth);			
			String level_ = request.getParameter("level_");
			int level_2 = Integer.parseInt(level_);
			
			BoardVo bv = new BoardVo();			
			bv.setBidx(bidx2);
			bv.setOriginbidx(originbidx2);
			bv.setDepth(depth2);
			bv.setLevel_(level_2);
			
			request.setAttribute("bv", bv);
			
			String path = "/boardReply.jsp";
			RequestDispatcher  rd = request.getRequestDispatcher(path);
			rd.forward(request, response);	
			
		}else if (str.equals("/board/boardReplyAction.do")) {
			String bidx = request.getParameter("bidx");
			int bidx2 = Integer.parseInt(bidx);   // 숫자형 변환			
			String originbidx = request.getParameter("originbidx");
			int originbidx2 = Integer.parseInt(originbidx);			
			String depth = request.getParameter("depth");
			int depth2 = Integer.parseInt(depth);			
			String level_ = request.getParameter("level_");
			int level_2 = Integer.parseInt(level_);			
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			
			BoardVo bv = new BoardVo();
			bv.setBidx(bidx2);
			bv.setOriginbidx(originbidx2);
			bv.setDepth(depth2);
			bv.setLevel_(level_2);
			bv.setSubject(subject);
			bv.setContents(contents);
			bv.setWriter(writer);
			bv.setMidx(1);	
			
			BoardDao bd = new BoardDao();
			int value = bd.boardReply(bv);			
			
			if (value ==1) {  //답변성공시
				String path =request.getContextPath()+"/board/boardList.do";   //send로이동시 외부경로 주소씀
				response.sendRedirect(path);
			}else { //답변 실패시
				String path =request.getContextPath()+"/board/boardContents.do?bidx="+bidx;   //send로이동시 외부경로 주소씀
				response.sendRedirect(path);	
			}
		}else if (str.equals("/board/fileDownload.do")) {
			//파일이름을 넘겨받기
			String filename = request.getParameter("filename");
			//파일의 전체경로
			String fullFilePath = filePath + "/"+filename; 
			
			//실제경로로 인식한다
			Path source = Paths.get(fullFilePath);
			String mimeType =    Files.probeContentType(source);   //파일형식 추출
			
			response.setContentType(mimeType);    //response객체에 파일형식을 담고
			
			//한글이름은 깨지지 않게
			String filenameEnCoding = new String(filename.getBytes("UTF-8")); 
			//헤더정보에 한글이름담기
			response.setHeader("Content-Disposition","attachment;fileName="+filenameEnCoding);
			//해당위치에 있는 파일을 읽어드린다
			FileInputStream fileInputStream = new FileInputStream(fullFilePath);
			//읽어드린 파일을 쓰기
			ServletOutputStream  so =   response.getOutputStream();
			
			//4바이트크기로
			byte[] b = new byte[4096];
			int read= 0;
			while ( (read = fileInputStream.read(b,0,b.length)) != -1  ) {
				so.write(b, 0, read);			//읽어드린 파일을 쓰기복사	
			}
		
			so.flush();
			so.close();
			fileInputStream.close();			
		}			
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
