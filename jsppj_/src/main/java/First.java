import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/First")
public class First extends HttpServlet {	
   	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
		//	out.println("Çï·Î ¿ùµå");
			
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Á¦¸ñ</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("¼­ºí¸´ Çï·Î ¿ùµå");
			out.println("</body>");
			out.println("</html>");			
	}	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doGet(request, response);
	}
}
