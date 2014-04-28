package no.srib.sribapp.controller;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.StreamUrlScheduleDAO;
import no.srib.sribapp.dao.interfaces.StreamurlDAO;
import no.srib.sribapp.model.Streamurl;
import no.srib.sribapp.model.Streamurlschedule;

/**
 * Servlet implementation class SetSource
 */
@WebServlet("/SetSource")
public class SetSource extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@EJB
	private StreamUrlScheduleDAO scheduleDAO;
	@EJB
	private StreamurlDAO streamurlDAO;
	
    public SetSource() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession ses = request.getSession(false);
        if(ses.getAttribute("loggedIn") != null){
            if(ses.getAttribute("loggedIn").equals("true")){
                
                List<Streamurl> streamUrlList = null;
                List<Streamurlschedule> streamScheduleList = null;
                Streamurl url1 = null;
                Streamurl url2 = null;
               String [] dayArray = {"Søndag","Mandag","Tirsdag","Onsdag","Torsdag","Fredag","Lørdag"};
               try {
                streamUrlList = streamurlDAO.getList();
                streamScheduleList = scheduleDAO.getList();
            } catch (DAOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
               if(streamUrlList != null){
                   url1 = streamUrlList.get(0);
                   url2 = streamUrlList.get(1);
               }
               
               request.setAttribute("days", dayArray);
               request.setAttribute("url1", url1);
               request.setAttribute("url2", url2);
               request.setAttribute("schedule", streamScheduleList);
               
                RequestDispatcher reqD = request.getRequestDispatcher("/WEB-INF/source.jsp");
                reqD.forward(request, response);
            
        }}
        else{
            response.sendRedirect("index.html");
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
