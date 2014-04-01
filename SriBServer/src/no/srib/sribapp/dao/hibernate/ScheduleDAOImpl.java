package no.srib.sribapp.dao.hibernate;

import no.srib.sribapp.dao.interfaces.ScheduleDAO;
import no.srib.sribapp.model.Schedule;

public class ScheduleDAOImpl extends AbstractModelDAOImpl<Schedule> implements
        ScheduleDAO {

    public ScheduleDAOImpl() {
        super(Schedule.class);
    }
}
