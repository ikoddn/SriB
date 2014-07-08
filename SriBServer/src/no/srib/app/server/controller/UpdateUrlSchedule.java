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
import no.srib.app.server.dao.interfaces.StreamUrlScheduleDAO;
import no.srib.app.server.model.jpa.Streamurlschedule;

/**
 * Servlet implementation class UpdateUrlSchedule
 */
@WebServlet("/UpdateUrlSchedule")
public class UpdateUrlSchedule extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private StreamUrlScheduleDAO streamUrlScheduleDAO;

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession ses = request.getSession();

        if (ses != null && ses.getAttribute("loggedIn") != null
                && ses.getAttribute("loggedIn").equals("true")) {

            int id = 0;
            int day = 0;
            Time fromTime = null;
            Time toTime = null;
            ses.setAttribute("errorUpdate", Boolean.valueOf(false));
            String idString = request.getParameter("id");
            String fromTimeString = request.getParameter("fromTime");
            String toTimeString = request.getParameter("toTime");
            String dayString = request.getParameter("day");

            if (idString != null && fromTimeString != null
                    && toTimeString != null && dayString != null) {
                id = Integer.parseInt(idString);
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
                    if (!fromTime.before(toTime)) {
                        throw new IllegalArgumentException();
                    }
                } catch (IllegalArgumentException e) {
                    ses.setAttribute("errorUpdate", Boolean.valueOf(true));
                    response.sendRedirect("/SriBServer/SetSource");
                    return;
                }

            }

            Streamurlschedule sus = new Streamurlschedule((byte) day, fromTime,
                    toTime);
            sus.setId(id);

            if (request.getParameter("edit") != null) {
                try {
                    streamUrlScheduleDAO.update(sus);
                } catch (DAOException e) {
                    e.printStackTrace();
                }

            } else if (request.getParameter("delete") != null) {
                try {
                    streamUrlScheduleDAO.remove(sus);
                } catch (DAOException e) {
                    e.printStackTrace();
                }

            }

            response.sendRedirect("/SriBServer/SetSource");
            return;

        } else {
            response.sendRedirect("index.html");
            return;

        }

    }

}
