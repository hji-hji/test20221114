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
		System.out.println("�Ѿ������?");
		
		//�ѱ۱�������
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
	//	String filePath ="D:\\dev0817\\eclipse-workspace\\uploadFiles\\";
		String filePath = "D:\\dev0817\\eclipse-workspace\\jsppj_\\src\\main\\webapp\\image\\";
		
		
		int fileSize = 15*1024*1024;  //15M
		String fileEncoding ="UTF-8";
		
		String uri = request.getRequestURI();   // ��ü �����ּ� ��θ� �޴´�
	    String pjName = 	request.getContextPath(); //������Ʈ �̸���θ� �����Ѵ�
		int jari = pjName.length();			//������Ʈ �̸� �ڸ����� �����Ѵ�
		String str = uri.substring(jari);		//������Ʈ �̸��� ������ �����ּҸ� �����Ѵ�
		
		System.out.println("board ������ �ּҴ�?"+str);
				
		if(str.equals("/board/boardList.do")) {
			//  /board/boardList.do �� �����η� ������
			//  ������ ������� boardList.jsp�� ��Ÿ���ش�
			
			//1.������ ��ȣ�� �Ѱ��ش�
			String page = request.getParameter("page");
			if (page == null) { 
				page = "1";
			}					
			String searchType = request.getParameter("searchType");  //�˻���� ����â
			if (searchType == null) {
				searchType="subject";
			}
			String keyword = request.getParameter("keyword");  //�˻���� Ű�����Է�â
			if (keyword ==null) {
				keyword = "";
			}			
				//����¡�� ������ �Ǵ� ��ü�� �����Ͽ� �������� �����Ѵ�
				SearchCriteria scri = new SearchCriteria();
				scri.setPage( Integer.parseInt(page) );	 // �Ѿ�� ���� ��������ȣ�� ��´�
				scri.setSearchType(searchType);
				scri.setKeyword(keyword);				
				
				BoardDao bd = new BoardDao();  
				ArrayList<BoardVo> alist = bd.boardList(scri);   //cri ��ü�� �Ű������� �ѱ��
				int cnt = bd.boardTotal(scri);  // ��ü �Խù� ���� ���Ѵ�
				
				//���̰��̼ǿ� �ʿ��� �������� ��� ��ü�� �����Ͽ�  cri��ü�� ��ü������ ��´�
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
			
			DefaultFileRenamePolicy reNamePolicy = new DefaultFileRenamePolicy();  //�����̸��ߺ���å
			
			MultipartRequest multiRequest = 
					new MultipartRequest(request,filePath,fileSize,fileEncoding,reNamePolicy);
						
			//�Է°��� �Ѱ��ش�
			String subject = multiRequest.getParameter("subject");
			String contents = multiRequest.getParameter("contents");
			String writer = multiRequest.getParameter("writer");
			
			//�����ڿ� ����� �����̸��� ��´�
			Enumeration files =   multiRequest.getFileNames();
			//�����ڿ� ��� �� ������ �Ű� ��´�
			String file = (String)files.nextElement();
			//����Ǵ� �̸��� ����
			String fileName = multiRequest.getFilesystemName(file);
			//���� ���� �̸� ����
			String originFileName= multiRequest.getOriginalFileName(file);
								
			//ip�� �����Ѵ�
			String ip = 	InetAddress.getLocalHost().getHostAddress();
			
			int midx = 1; 
			
			BoardDao bd = new BoardDao();
			int value = bd.boardInsert(subject, contents, writer, ip, midx, fileName);
						
			if (value==1) {  //�Է¼���
			String path =request.getContextPath()+"/board/boardList.do";   //send���̵��� �ܺΰ�� �ּҾ�
			response.sendRedirect(path);
			}else {  //�Է½���
			String path =request.getContextPath()+"/board/boardWrite.do";   //send���̵��� �ܺΰ�� �ּҾ�
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
			
			if (value ==1) {  //����������
				String path =request.getContextPath()+"/board/boardContents.do?bidx="+bidx;   //send���̵��� �ܺΰ�� �ּҾ�
				response.sendRedirect(path);
			}else { //���� ���н�
				String path =request.getContextPath()+"/board/boardModify.do?bidx="+bidx;   //send���̵��� �ܺΰ�� �ּҾ�
				response.sendRedirect(path);	
			}
		}else if (str.equals("/board/boardDelete.do")) {
			//�����η� �Ѿ��
			String bidx =  request.getParameter("bidx");
			
			//request��ü��  bidx�̸����� bidx���� ��Ƽ�  jspȭ�鿡�� �����Ѵ�
			 request.setAttribute("bidx", bidx);
			//�����̵� ����
			//����ȭ���� ������
			String path = "/boardDelete.jsp";
			RequestDispatcher  rd = request.getRequestDispatcher(path);
			rd.forward(request, response);			
		}else if (str.equals("/board/boardDeleteAction.do")) {
			String bidx = request.getParameter("bidx");
			int bidx2 = Integer.parseInt(bidx);			
			
			BoardDao bd = new BoardDao();
			int value = bd.boardDelete(bidx2);			
			
			if (value ==1) {  //����������
				String path =request.getContextPath()+"/board/boardList.do";   //send���̵��� �ܺΰ�� �ּҾ�
				response.sendRedirect(path);
			}else { //���� ���н�
				String path =request.getContextPath()+"/board/boardDelete.do?bidx="+bidx;   //send���̵��� �ܺΰ�� �ּҾ�
				response.sendRedirect(path);	
			}
			
		}else if (str.equals("/board/boardReply.do")) {
			
			String bidx = request.getParameter("bidx");
			int bidx2 = Integer.parseInt(bidx);   // ������ ��ȯ			
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
			int bidx2 = Integer.parseInt(bidx);   // ������ ��ȯ			
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
			
			if (value ==1) {  //�亯������
				String path =request.getContextPath()+"/board/boardList.do";   //send���̵��� �ܺΰ�� �ּҾ�
				response.sendRedirect(path);
			}else { //�亯 ���н�
				String path =request.getContextPath()+"/board/boardContents.do?bidx="+bidx;   //send���̵��� �ܺΰ�� �ּҾ�
				response.sendRedirect(path);	
			}
		}else if (str.equals("/board/fileDownload.do")) {
			//�����̸��� �Ѱܹޱ�
			String filename = request.getParameter("filename");
			//������ ��ü���
			String fullFilePath = filePath + "/"+filename; 
			
			//������η� �ν��Ѵ�
			Path source = Paths.get(fullFilePath);
			String mimeType =    Files.probeContentType(source);   //�������� ����
			
			response.setContentType(mimeType);    //response��ü�� ���������� ���
			
			//�ѱ��̸��� ������ �ʰ�
			String filenameEnCoding = new String(filename.getBytes("UTF-8")); 
			//��������� �ѱ��̸����
			response.setHeader("Content-Disposition","attachment;fileName="+filenameEnCoding);
			//�ش���ġ�� �ִ� ������ �о�帰��
			FileInputStream fileInputStream = new FileInputStream(fullFilePath);
			//�о�帰 ������ ����
			ServletOutputStream  so =   response.getOutputStream();
			
			//4����Ʈũ���
			byte[] b = new byte[4096];
			int read= 0;
			while ( (read = fileInputStream.read(b,0,b.length)) != -1  ) {
				so.write(b, 0, read);			//�о�帰 ������ ���⺹��	
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
