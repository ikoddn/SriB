package no.srib.sribapp.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.ejb.EJB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.StreamurlDAO;
import no.srib.sribapp.model.Streamurl;

/**
 * Servlet implementation class UpdateUrl
 */
@WebServlet("/UpdateUrl")
public class UpdateUrl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private StreamurlDAO streamurldao;

    public UpdateUrl() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        HttpSession ses = request.getSession();
        
        if (ses != null) {
            ses.setAttribute("errorUrl", new Boolean(false));
            if (ses.getAttribute("loggedIn").equals("true")) {
                String url = request.getParameter("url");
                String name = request.getParameter("name");
                String idString = request.getParameter("id");
                System.out.println(idString);
               
                
                try {
                  new URL(url);
                  
                } catch (MalformedURLException e) {
                    ses.setAttribute("errorUrl", new Boolean(true));
                    response.sendRedirect("/SriBServer/SetSource");
                    return;
                }
                
                if (url != null && name != null && idString != null ) {
                    int id = Integer.valueOf(idString);
                    Streamurl streamUrl = new Streamurl();
                      streamUrl.setId(id);
                      streamUrl.setName(name);
                      streamUrl.setUrl(url);
                    
                   try {
                    streamurldao.updateElement(streamUrl);
                } catch (DAOException e) {
                    ses.setAttribute("errorUrl", new Boolean(true));
                    response.sendRedirect("/SriBServer/SetSource");
                    e.printStackTrace();
                }
                    
                response.sendRedirect("/SriBServer/SetSource");
                return;
                   
                } else {
                    ses.setAttribute("errorUrl", new Boolean(true));
                    response.sendRedirect("/SriBServer/SetSource");
                    return;
                }
            }else{
                response.sendRedirect("index.html");
                return;
            }
        }
    }
}
