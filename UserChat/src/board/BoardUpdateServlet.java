package board;

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

public class BoardUpdateServlet extends HttpServlet {
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
			response.sendRedirect("index.jsp");
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
		
		String boardID = multi.getParameter("boardID");
		if(boardID == null || boardID.equals("")) {
			session.setAttribute("messageType", "���� �޼���");
			session.setAttribute("messageContent", "������ �� �����.");
			response.sendRedirect("index.jsp");
			return;
		}
		
		String boardTitle = multi.getParameter("boardTitle");
		String boardContent = multi.getParameter("boardContent");
		
		if(boardTitle.equals("") || boardTitle == null || boardContent.equals("") || boardTitle == null) {
			session.setAttribute("messageType", "���� �޼���");
			session.setAttribute("messageContent", "������ ��� ä���ּ���.");
			response.sendRedirect("boardWrite.jsp");
			return;
		}
		BoardDAO boardDAO = new BoardDAO();
		BoardDTO board = boardDAO.getBoard(boardID);
		//�ڱ� �ڽ��� �ƴ� ���
		if(!userID.equals(board.getUserID())) {
			session.setAttribute("messageType", "���� �޼���");
			session.setAttribute("messageContent", "������ �� �����.");
			response.sendRedirect("index.jsp");
			return;
		}
		
		String boardFile = "";
		String boardRealFile = "";
		File file = multi.getFile("boardFile");
		
		//������ ���Ӱ� �ִ� ��� ���������� �����ش�.
		if(file != null) {
			 boardFile = multi.getOriginalFileName("boardFile");
			 boardRealFile = file.getName();
			 String prev = boardDAO.getRealFile(boardID);
			 File prevFile = new File(savePath + "/" + prev);
			 if(prevFile.exists()) {
				 prevFile.delete();
			 }
		} else {
			boardFile = boardDAO.getFile(boardID);
			boardRealFile = boardDAO.getRealFile(boardID);
		}
		boardDAO.update(boardID, boardTitle, boardContent, boardFile, boardRealFile);
		
		request.getSession().setAttribute("messageType", "���� �޼���");
		request.getSession().setAttribute("messageContent", "���������� �Խù��� �����Ǿ����ϴ�.");
		response.sendRedirect("boardView.jsp");
		return;
	}

}