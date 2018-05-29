package user;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.sun.xml.internal.fastinfoset.util.PrefixArray;

/**
 * Servlet implementation class UserProfileServlet
 */
@WebServlet("/UserProfileServlet")
public class UserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		
		MultipartRequest multi = null;
		int fileMaxSize = 10 * 1024 * 1024;
		String savePath = request.getRealPath("/upload").replace("\\\\", "/");
		
		try {
			multi = new MultipartRequest(request, savePath, fileMaxSize, "UTF-8", new DefaultFileRenamePolicy());
		} catch (Exception e) {
			// TODO: handle exception
			request.getSession().setAttribute("messageType", "���� �޼���");
			request.getSession().setAttribute("messageContent", "���� ũ��� 10MB�� ���� �� �����.");
			response.sendRedirect("profileUpdate.jsp");
			return;
		}
		
		String userID = multi.getParameter("userID");
		HttpSession session = request.getSession();
		
		//�ٸ� ����� ������ �� ������ ó��
		if(!userID.equals((String) session.getAttribute("userID"))) {
			session.setAttribute("messageType", "���� �޼���");
			session.setAttribute("messageContent", "������ �� �����.");
			response.sendRedirect("index.jsp");
			return;
		}
		
		String fileName = "";
		File file = multi.getFile("userProfile");
		
		if(file != null) {
			//Ȯ���� ���� ������
			String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
			if(ext.equals("jpg") || ext.equals("png") || ext.equals("gif")) {
				String prev = new UserDAO().getUser(userID).getUserProfile();
				//���� ������ �����ش�.
				File prevFile = new File(savePath + "/" + prev);
				//������ �����ϸ� �����ش�.
				if(prevFile.exists()) {
					prevFile.delete();
				}
				fileName = file.getName();	
			} else {
				//�̹��� ���ϸ� �����ϵ��� �ϱ�
				if(file.exists()) {
					file.delete();
				}
				request.getSession().setAttribute("messageType", "���� �޼���");
				request.getSession().setAttribute("messageContent", "�̹��� ���ϸ� ���ε� �����ؿ�.");
				response.sendRedirect("profileUpdate.jsp");
				return;
			}
		}
		//���������� ���� �κ�
		new UserDAO().profile(userID, fileName);
		request.getSession().setAttribute("messageType", "���� �޼���");
		request.getSession().setAttribute("messageContent", "���������� �������� ���� �Ǿ����ϴ�.");
		response.sendRedirect("index.jsp");
		return;
	}

}
