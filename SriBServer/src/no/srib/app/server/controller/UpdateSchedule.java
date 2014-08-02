package no.srib.app.server.controller;

import java.io.IOException;
import java.sql.Time;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.srib.app.server.dao.exception.DAOException;
import no.srib.app.server.dao.interfaces.ScheduleDAO;
import no.srib.app.server.model.jpa.Schedule;

/**
 * Servlet implementation class UpdateSchedule
 */
@WebServlet("/UpdateSchedule")
public class UpdateSchedule extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private ScheduleDAO scheduleDAO;

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,

    HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession ses = request.getSession(false);

        if (ses != null && ses.getAttribute("loggedIn") != null
                && ses.getAttribute("loggedIn").equals("true")) {
            ses.setAttribute("errorUpdate", Boolean.valueOf(false));
            Time fromTime = null;
            Time toTime = null;
            int day = 0;
            int id = 0;
            int program = 0;

            String idString = request.getParameter("id");
            String toTimeString = request.getParameter("toTime");
            String fromTimeString = request.getParameter("fromTime");
            String dayString = request.getParameter("day");
            String programString = request.getParameter("program");

            if (fromTimeString != null && toTimeString != null
                    && dayString != null && programString != null) {

                day = Integer.parseInt(dayString);
                try {
                    if (fromTimeString.length() == 5) {
                        fromTimeString += ":00";
                    }
                    if (toTimeString.length() == 5) {
                        toTimeString += ":00";
                    }
                    fromTime = Time.valueOf(fromTimeString);
                    toTime = Time.valueOf(toTimeString);
                    program = Integer.parseInt(programString);
                    if (!fromTime.before(toTime)) {
                        throw new IllegalArgumentException();
                    }
                } catch (IllegalArgumentException e) {
                    ses.setAttribute("errorUpdate", Boolean.valueOf(true));
                    response.sendRedirect("/SriBServer/SetSchedule");
                    return;
                }

                Schedule sch = new Schedule((byte) day, fromTime, toTime,
                        program);

                if (request.getParameter("add") != null) {
                    try {
                        scheduleDAO.add(sch);
                    } catch (DAOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {
                    if (idString != null) {
                        id = Integer.parseInt(idString);
                        sch.setId(id);
                    }
                    if (request.getParameter("edit") != null) {

                        try {
                            scheduleDAO.update(sch);
                        } catch (DAOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    } else if (request.getParameter("delete") != null) {
                        try {
                            scheduleDAO.remove(sch);
                        } catch (DAOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

                response.sendRedirect("SetSchedule");

            } else {
                response.sendRedirect("index.html");
            }
        }

    }
}
