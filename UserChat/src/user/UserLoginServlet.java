package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserLoginServlet
 */
@WebServlet("/UserLoginServlet")
public class UserLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		String userID = request.getParameter("userID");
		String userPassword = request.getParameter("userPassword");
		
		if(userID == null || userID.equals("") || userPassword == null || userPassword.equals("")) {
			request.getSession().setAttribute("messageType", "���� �޼���");
			request.getSession().setAttribute("messageContent", "��� ������ �Է����ּ���.");
			response.sendRedirect("login.jsp");
			return;
		}
		int result = new UserDAO().login(userID, userPassword);
		if(result == 1) {
			request.getSession().setAttribute("userID", userID);
			request.getSession().setAttribute("messageType", "���� �޼���");
			request.getSession().setAttribute("messageContent", "�α��ο� �����߽��ϴ�.");
			response.sendRedirect("index.jsp");
		} else if(result == 2) {
			request.getSession().setAttribute("messageType", "���� �޼���");
			request.getSession().setAttribute("messageContent", "��й�ȣ�� Ȯ���ϼ���.");
			response.sendRedirect("login.jsp");
		} else if(result == 0) {
			request.getSession().setAttribute("messageType", "���� �޼���");
			request.getSession().setAttribute("messageContent", "���̵� �������� �ʽ��ϴ�.");
			response.sendRedirect("login.jsp");
		} else if(result == -1) {
			request.getSession().setAttribute("messageType", "���� �޼���");
			request.getSession().setAttribute("messageContent", "�����ͺ��̽� ������ �߻��߽��ϴ�.");
			response.sendRedirect("login.jsp");
		}
		
	}

}