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
			request.getSession().setAttribute("messageType", "오류 메세지");
			request.getSession().setAttribute("messageContent", "파일 크기는 10MB를 넘을 수 없어요.");
			response.sendRedirect("profileUpdate.jsp");
			return;
		}
		
		String userID = multi.getParameter("userID");
		HttpSession session = request.getSession();
		
		//다른 사람은 수정할 수 없도록 처리
		if(!userID.equals((String) session.getAttribute("userID"))) {
			session.setAttribute("messageType", "오류 메세지");
			session.setAttribute("messageContent", "접근할 수 없어요.");
			response.sendRedirect("index.jsp");
			return;
		}
		
		String fileName = "";
		File file = multi.getFile("userProfile");
		
		if(file != null) {
			//확장자 명을 가져옴
			String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
			if(ext.equals("jpg") || ext.equals("png") || ext.equals("gif")) {
				String prev = new UserDAO().getUser(userID).getUserProfile();
				//기존 파일을 지워준다.
				File prevFile = new File(savePath + "/" + prev);
				//파일이 존재하면 지워준다.
				if(prevFile.exists()) {
					prevFile.delete();
				}
				fileName = file.getName();	
			} else {
				//이미지 파일만 가능하도록 하기
				if(file.exists()) {
					file.delete();
				}
				request.getSession().setAttribute("messageType", "오류 메세지");
				request.getSession().setAttribute("messageContent", "이미지 파일만 업로드 가능해요.");
				response.sendRedirect("profileUpdate.jsp");
				return;
			}
		}
		//정상적으로 들어가는 부분
		new UserDAO().profile(userID, fileName);
		request.getSession().setAttribute("messageType", "성공 메세지");
		request.getSession().setAttribute("messageContent", "성공적으로 프로필이 변경 되었습니다.");
		response.sendRedirect("index.jsp");
		return;
	}

}
