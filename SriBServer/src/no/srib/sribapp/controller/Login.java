package no.srib.sribapp.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;





/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   
	    
	    HttpSession ses =  request.getSession(false);
	    if(ses == null){
            ses = request.getSession();
            
        }else{
           if(ses.getAttribute("loggedIn").equals("true")){
               RequestDispatcher reqD = request.getRequestDispatcher("/WEB-INF/admin.html");
               reqD.forward(request, response);
           }
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		HttpSession ses =  request.getSession(false);
		if(ses == null){
		    ses = request.getSession();
		    
		}else{
		   if(ses.getAttribute("loggedIn").equals("true") && username.equals("test") && password.equals("test")){
		       RequestDispatcher reqD = request.getRequestDispatcher("/WEB-INF/admin.html");
               reqD.forward(request, response);
		   }else{
		       ses.setAttribute("loggedIn", "false");
		   }
		}
		
		if(username.equals("test") && password.equals("test")){
		    ses.setAttribute("loggedIn", "true");
		    RequestDispatcher reqD = request.getRequestDispatcher("/WEB-INF/admin.html");
            reqD.forward(request, response);
		}else{
		    response.sendRedirect("index.html");
		}
		
		
	}

}
