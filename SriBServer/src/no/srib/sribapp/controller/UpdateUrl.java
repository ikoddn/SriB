package no.srib.sribapp.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.srib.sribapp.dao.interfaces.StreamurlDAO;

/**
 * Servlet implementation class UpdateUrl
 */
@WebServlet("/UpdateUrl")
public class UpdateUrl extends HttpServlet {
    private static final long serialVersionUID = 1L;

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
                String url1 = request.getParameter("mainSource");
                String url2 = request.getParameter("secondSource");
                
                
                try {
                  URL urlTest1 = new URL(url1);
                  URL urlTest2 = new URL(url2);
                } catch (MalformedURLException e) {
                  // it wasn't a URL
                }
                
                if (url1 != null && url2 != null ) {
                       
                   // streamurldao.addElement(el);
                    
                    
                } else {
                    ses.setAttribute("errorUrl", new Boolean(true));
                    response.sendRedirect("SribServer/SetSource");
                }
            }else{
                response.sendRedirect("index.html");
            }
        }
    }
}
