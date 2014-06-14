package no.srib.app.server.controller;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.srib.app.server.dao.exception.DAOException;
import no.srib.app.server.dao.exception.DuplicateEntryException;
import no.srib.app.server.dao.interfaces.ProgramnameDAO;
import no.srib.app.server.model.jpa.Programname;

/**
 * Servlet implementation class AddProgram
 */
@WebServlet("/AddProgram")
public class AddProgram extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private ProgramnameDAO pDAO;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        HttpSession ses = request.getSession();

        if (ses != null && ses.getAttribute("loggedIn") != null
                && ses.getAttribute("loggedIn").equals("true")) {

            List<Programname> pList = null;

            try {
                pList = pDAO.getSortedList();
            } catch (DAOException e) {

                e.printStackTrace();
            }

            request.setAttribute("programList", pList);

            request.getRequestDispatcher("WEB-INF/addprogram.jsp").forward(
                    request, response);
        } else {
            response.sendRedirect("index.html");
            return;
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession ses = request.getSession();
        ses.setAttribute("error", new Boolean(false));
        ses.setAttribute("errorDuplicate", new Boolean(false));
        if (ses != null && ses.getAttribute("loggedIn") != null
                && ses.getAttribute("loggedIn").equals("true")) {

            String name = request.getParameter("name");

            if (name != null && name.length() > 0) {

            } else {
                ses.setAttribute("error", new Boolean(true));
                response.sendRedirect("/SriBServer/AddProgram");
                return;
            }
            Programname pName = new Programname();
            pName.setName(name);
            if (request.getParameter("add") != null) {

                try {
                    pDAO.add(pName);
                } catch (DuplicateEntryException e) {
                    ses.setAttribute("errorDuplicate", new Boolean(true));
                    response.sendRedirect("AddProgram");
                    return;
                }

                catch (DAOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }
            } else {
                String idString = request.getParameter("id");
                int id = 0;
                if (idString != null) {
                    id = Integer.parseInt(idString);
                }

                pName.setId(id);

                if (request.getParameter("edit") != null) {

                    try {
                        pDAO.add(pName);
                    } catch (DuplicateEntryException e) {
                        ses.setAttribute("errorDuplicate", new Boolean(true));
                        response.sendRedirect("AddProgram");
                        return;
                    } catch (DAOException e) {
                        e.printStackTrace();
                    }

                } else if (request.getParameter("delete") != null) {

                    try {
                        pDAO.remove(pName);
                    } catch (DAOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }

            response.sendRedirect("AddProgram");
            return;
        } else {
            response.sendRedirect("index.html");
        }
    }

}
