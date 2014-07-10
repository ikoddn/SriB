package no.srib.app.server.util;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class TimeUtilTest {

    private Calendar now;
    private Calendar timeCalendar;

    @Before
    public void before() {
        now = Calendar.getInstance();
        timeCalendar = Calendar.getInstance();
        timeCalendar.setTimeInMillis(0);
    }

    @Test
    public void calendarTimeCompareBeforeOnSameDay() {
        now.set(Calendar.HOUR_OF_DAY, 20);
        timeCalendar.set(Calendar.HOUR_OF_DAY, 21);

        java.sql.Time time = new java.sql.Time(timeCalendar.getTimeInMillis());

        assertTrue(TimeUtil.compare(now, time) < 0);
    }

    @Test
    public void calendarTimeCompareLaterOnSameDay() {
        now.set(Calendar.HOUR_OF_DAY, 18);
        timeCalendar.set(Calendar.HOUR_OF_DAY, 17);

        java.sql.Time time = new java.sql.Time(timeCalendar.getTimeInMillis());

        assertTrue(TimeUtil.compare(now, time) > 0);
    }

    @Test
    public void calendarTimeCompareSameTimeOnSameDay() {
        now.set(Calendar.HOUR_OF_DAY, 9);
        now.set(Calendar.MINUTE, 10);
        now.set(Calendar.SECOND, 11);
        timeCalendar.set(Calendar.HOUR_OF_DAY, 9);
        timeCalendar.set(Calendar.MINUTE, 10);
        timeCalendar.set(Calendar.SECOND, 11);

        java.sql.Time time = new java.sql.Time(timeCalendar.getTimeInMillis());

        assertEquals(0, TimeUtil.compare(now, time));
    }
}
