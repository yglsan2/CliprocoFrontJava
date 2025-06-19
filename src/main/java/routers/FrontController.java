package routers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/")
public class FrontController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        
        if (cmd == null || cmd.isEmpty()) {
            cmd = "index";
        }
        
        switch (cmd) {
            case "index":
                request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
                break;
            case "connexion":
                request.getRequestDispatcher("/WEB-INF/jsp/connexion.jsp").forward(request, response);
                break;
            case "clients":
                request.getRequestDispatcher("/WEB-INF/jsp/clients.jsp").forward(request, response);
                break;
            case "prospects":
                request.getRequestDispatcher("/WEB-INF/jsp/prospects.jsp").forward(request, response);
                break;
            case "contact":
                request.getRequestDispatcher("/WEB-INF/jsp/contact.jsp").forward(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
