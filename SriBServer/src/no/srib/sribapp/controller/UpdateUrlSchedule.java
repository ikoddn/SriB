package no.srib.sribapp.controller;

import java.io.IOException;
import java.sql.Time;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.StreamUrlScheduleDAO;
import no.srib.sribapp.model.Streamurlschedule;

/**
 * Servlet implementation class UpdateUrlSchedule
 */
@WebServlet("/UpdateUrlSchedule")
public class UpdateUrlSchedule extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private StreamUrlScheduleDAO streamUrlScheduleDAO;

    public UpdateUrlSchedule() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        HttpSession ses = request.getSession();

        if (ses.getAttribute("loggedIn") != null) {
            if (ses.getAttribute("loggedIn").equals("true")) {
                int id = 0;
                int day = 0;
                Time fromTime = null;
                Time toTime = null;
                ses.setAttribute("errorUpdate", new Boolean(false));
                String idString = request.getParameter("id");
                String fromTimeString = request.getParameter("fromTime");
                String toTimeString = request.getParameter("toTime");
                String dayString = request.getParameter("day");

                if (idString != null && fromTimeString != null
                        && toTimeString != null && dayString != null) {
                    id = Integer.parseInt(idString);
                    day = Integer.parseInt(dayString);
                    try {
                        if(fromTimeString.length() == 5 ){
                            fromTimeString += ":00";
                        }if(toTimeString.length() == 5){
                            toTimeString += ":00";
                        }
                        fromTime = Time.valueOf(fromTimeString);
                        toTime = Time.valueOf(toTimeString);
                        if(!fromTime.before(toTime)){
                            throw new IllegalArgumentException();
                        }
                    } catch (IllegalArgumentException e) {
                        ses.setAttribute("errorUpdate", new Boolean(true));
                        response.sendRedirect("/SriBServer/SetSource");
                        return;
                    }

                }
                Streamurlschedule sus = new Streamurlschedule();
                sus.setDay((byte)day);
                sus.setId(id);
                sus.setTotime(toTime);
                sus.setFromtime(fromTime);
                if (request.getParameter("edit") != null) {
                       try {
                        streamUrlScheduleDAO.updateElement(sus);
                    } catch (DAOException e) {
                        e.printStackTrace();
                    }
                    
                } else if (request.getParameter("delete") != null) {
                   try {
                    streamUrlScheduleDAO.removeElement(sus);
                } catch (DAOException e) {
                    e.printStackTrace();
                }

                }

                response.sendRedirect("/SriBServer/SetSource");
                return;
            }
        } else {
            response.sendRedirect("index.html");
            return;

        }

    }

}
