package no.srib.sribapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.hibernate.DefinitionDAOImpl;
import no.srib.sribapp.dao.hibernate.ScheduleDAOImpl;
import no.srib.sribapp.dao.interfaces.DefinitionDAO;
import no.srib.sribapp.dao.interfaces.ScheduleDAO;
import no.srib.sribapp.model.Definition;
import no.srib.sribapp.model.Schedule;

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

                ScheduleDAO scheduleDAO = new ScheduleDAOImpl();
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
                
               
                Map<Integer,Definition> definitionMap = new HashMap<Integer,Definition>();
                for(Definition def : defList){
                    definitionMap.put(def.getDefnr(), def);
                }
                
                
               for(Schedule schedule : scheduleList){
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
               
               for(Schedule a : scheduleList){
                   System.out.println(a.getFromtime() + " " + a.getDay());
                   
               }
               
               request.setAttribute("mondayList", mondayList);
               request.setAttribute("tuesdayList", tuesdayList);
               request.setAttribute("wednesdayList", wednesdayList);
               request.setAttribute("thursdayList", thursdayList);
               request.setAttribute("fridayList", fridayList);
               request.setAttribute("saturdayList", saturdayList);
               request.setAttribute("sundayList", sundayList);
               request.setAttribute("definitionMap", definitionMap);
               
                Definition def = new Definition();

                def.setName("Velg program");
                defList.add(0, def);
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
