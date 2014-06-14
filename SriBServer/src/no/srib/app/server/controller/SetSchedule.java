package no.srib.app.server.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.srib.app.server.dao.exception.DAOException;
import no.srib.app.server.dao.interfaces.ProgramnameDAO;
import no.srib.app.server.dao.interfaces.ScheduleDAO;
import no.srib.app.server.model.jpa.Programname;
import no.srib.app.server.model.jpa.Schedule;

/**
 * Servlet implementation class Schedule
 */
@WebServlet("/SetSchedule")
public class SetSchedule extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private ScheduleDAO scheduleDAO;

    @EJB
    private ProgramnameDAO pDAO;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        HttpSession ses = request.getSession(false);

        if (ses != null && ses.getAttribute("loggedIn") != null
                && ses.getAttribute("loggedIn").equals("true")) {

            List<Programname> progList = null;

            try {
                progList = pDAO.getSortedList();
            } catch (DAOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            List<Schedule> scheduleList = null;
            try {
                scheduleList = scheduleDAO.getSortedSchedule();
            } catch (DAOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            List<Schedule> mondayList = new ArrayList<Schedule>();
            List<Schedule> tuesdayList = new ArrayList<Schedule>();
            List<Schedule> wednesdayList = new ArrayList<Schedule>();
            List<Schedule> thursdayList = new ArrayList<Schedule>();
            List<Schedule> fridayList = new ArrayList<Schedule>();
            List<Schedule> saturdayList = new ArrayList<Schedule>();
            List<Schedule> sundayList = new ArrayList<Schedule>();
            String[] dayArray = { "Mandag", "Tirsdag", "Onsdag", "Torsdag",
                    "Fredag", "Lørdag", "Søndag" };

            Map<Integer, Programname> definitionMap = new HashMap<Integer, Programname>();
            for (Programname prog : progList) {
                definitionMap.put(prog.getId(), prog);
            }

            for (Schedule schedule : scheduleList) {
                switch (schedule.getDay()) {
                case Calendar.MONDAY:
                    mondayList.add(schedule);
                    break;
                case Calendar.TUESDAY:
                    tuesdayList.add(schedule);
                    break;
                case Calendar.WEDNESDAY:
                    wednesdayList.add(schedule);
                    break;
                case Calendar.THURSDAY:
                    thursdayList.add(schedule);
                    break;
                case Calendar.FRIDAY:
                    fridayList.add(schedule);
                    break;
                case Calendar.SATURDAY:
                    saturdayList.add(schedule);
                    break;
                case Calendar.SUNDAY:
                    sundayList.add(schedule);
                    break;
                default:
                    break;
                }
            }

            List<List<Schedule>> list = new ArrayList<List<Schedule>>();
            list.add(mondayList);
            list.add(tuesdayList);
            list.add(wednesdayList);
            list.add(thursdayList);
            list.add(fridayList);
            list.add(saturdayList);
            list.add(sundayList);

            // Default program when program is not set.
            Programname def = new Programname();
            def.setName("Velg program");
            progList.add(0, def);

            request.setAttribute("dbList", list);
            request.setAttribute("days", dayArray);
            request.setAttribute("definitionMap", definitionMap);
            request.setAttribute("programlist", progList);
            RequestDispatcher reqD = request
                    .getRequestDispatcher("/WEB-INF/schedule.jsp");
            reqD.forward(request, response);

        } else {
            response.sendRedirect("index.html");
        }

    }
}
