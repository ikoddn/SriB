package no.srib.sribapp.controller;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.ScheduleDAO;
import no.srib.sribapp.dao.jpa.ScheduleDAOImpl;

/**
 * Servlet implementation class DeleteScheduleEntry
 */
@WebServlet("/DeleteScheduleEntry")
public class DeleteScheduleEntry extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private ScheduleDAO scheduleDAO;
    
    public DeleteScheduleEntry() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        int idInt = 0;
        String id = request.getParameter("id");
        if (id != null) {
            idInt = Integer.parseInt(id);
        }
       
        try {
            scheduleDAO.deleteSchedule(idInt);
        } catch (DAOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

}
