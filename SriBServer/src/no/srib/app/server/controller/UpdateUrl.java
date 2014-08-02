package no.srib.app.server.controller;

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

import no.srib.app.server.dao.exception.DAOException;
import no.srib.app.server.dao.interfaces.StreamurlDAO;
import no.srib.app.server.model.jpa.Streamurl;

/**
 * Servlet implementation class UpdateUrl
 */
@WebServlet("/UpdateUrl")
public class UpdateUrl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private StreamurlDAO streamurldao;

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession ses = request.getSession();

        if (ses != null) {
            ses.setAttribute("errorUrl", Boolean.valueOf(false));
            if (ses.getAttribute("loggedIn").equals("true")) {
                String url = request.getParameter("url");
                String name = request.getParameter("name");
                String idString = request.getParameter("id");

                try {
                    new URL(url);
                } catch (MalformedURLException e) {
                    ses.setAttribute("errorUrl", Boolean.valueOf(true));
                    response.sendRedirect("/SriBServer/SetSource");
                    return;
                }

                if (name != null && idString != null) {
                    int id = Integer.parseInt(idString);
                    Streamurl streamUrl = new Streamurl(name, url);
                    streamUrl.setId(id);

                    try {
                        streamurldao.update(streamUrl);
                    } catch (DAOException e) {
                        ses.setAttribute("errorUrl", Boolean.valueOf(true));
                        response.sendRedirect("/SriBServer/SetSource");
                        e.printStackTrace();
                    }

                    response.sendRedirect("/SriBServer/SetSource");
                    return;

                } else {
                    ses.setAttribute("errorUrl", Boolean.valueOf(true));
                    response.sendRedirect("/SriBServer/SetSource");
                    return;
                }
            } else {
                response.sendRedirect("index.html");
                return;
            }
        }
    }
}
