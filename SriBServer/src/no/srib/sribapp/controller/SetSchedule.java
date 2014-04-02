package no.srib.sribapp.controller;

import java.io.IOException;

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.hibernate.DefinitionDAOImpl;
import no.srib.sribapp.dao.interfaces.DefinitionDAO;
import no.srib.sribapp.model.Definition;

/**
 * Servlet implementation class Schedule
 */
@WebServlet("/SetSchedule")
public class SetSchedule extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetSchedule() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        HttpSession ses = request.getSession(false);
        if (ses != null) {
            if (ses.getAttribute("loggedIn").equals("true")) {
                
                List<Definition> defList = null;
                DefinitionDAO dao = new DefinitionDAOImpl();
                try {
                    defList = dao.getList();
                } catch (DAOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (defList != null) {
                    for (Definition def : defList) {
                        System.out.println(def.getName());
                        
                    }
                }
                Definition def = new Definition();
                def.setName("Velg program");
                defList.add(0,def);
                request.setAttribute("programlist", defList);
                RequestDispatcher reqD = request
                        .getRequestDispatcher("/WEB-INF/schedule.jsp");
                reqD.forward(request, response);
            }
        } else {
            response.sendRedirect("index.html");
        }

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

    }

}
