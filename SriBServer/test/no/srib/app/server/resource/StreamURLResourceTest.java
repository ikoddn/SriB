package no.srib.app.server.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import no.srib.app.server.model.jpa.Streamurlschedule;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StreamURLResourceTest {

    private static java.sql.Time time1500;
    private static java.sql.Time time2100;

    private Calendar calendar;

    @BeforeClass
    public static void beforeClass() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);

        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        time1500 = new java.sql.Time(calendar.getTimeInMillis());

        calendar.set(Calendar.HOUR_OF_DAY, 21);
        time2100 = new java.sql.Time(calendar.getTimeInMillis());
    }

    @Before
    public void before() {
        calendar = Calendar.getInstance();
    }

    @Test
    public void nextStreamURLChangeTimeBeforeFromTimeOnSameDay() {
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        Streamurlschedule schedule = new Streamurlschedule((byte) dayOfWeek,
                time1500, time2100);
        Calendar changeTime = StreamURLResource.nextStreamURLChangeTime(
                calendar, schedule);

        assertTrue(changeTime.after(calendar));
        assertEquals(dayOfWeek, changeTime.get(Calendar.DAY_OF_WEEK));
        assertEquals(dayOfYear, changeTime.get(Calendar.DAY_OF_YEAR));
        assertEquals(15, changeTime.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void nextStreamURLChangeTimeBetweenFromTimeAndToTimeOnSameDay() {
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        Streamurlschedule schedule = new Streamurlschedule((byte) dayOfWeek,
                time1500, time2100);
        Calendar changeTime = StreamURLResource.nextStreamURLChangeTime(
                calendar, schedule);

        assertTrue(changeTime.after(calendar));
        assertEquals(dayOfWeek, changeTime.get(Calendar.DAY_OF_WEEK));
        assertEquals(dayOfYear, changeTime.get(Calendar.DAY_OF_YEAR));
        assertEquals(21, changeTime.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void nextStreamURLChangeTimeAfterToTimeOnSameDay() {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        Streamurlschedule schedule = new Streamurlschedule((byte) dayOfWeek,
                time1500, time2100);
        Calendar changeTime = StreamURLResource.nextStreamURLChangeTime(
                calendar, schedule);

        assertTrue(changeTime.after(calendar));
        assertEquals(dayOfWeek, changeTime.get(Calendar.DAY_OF_WEEK));
        assertFalse(dayOfYear == changeTime.get(Calendar.DAY_OF_YEAR));
        assertEquals(15, changeTime.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void nextStreamURLChangeTimeBeforeFromTimeBeforeDayOfWeek() {
        int calendarDayOfWeek = Calendar.TUESDAY;
        int scheduleDayOfWeek = Calendar.FRIDAY;

        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.DAY_OF_WEEK, calendarDayOfWeek);

        Streamurlschedule schedule = new Streamurlschedule(
                (byte) scheduleDayOfWeek, time1500, time2100);
        Calendar changeTime = StreamURLResource.nextStreamURLChangeTime(
                calendar, schedule);

        assertTrue(changeTime.after(calendar));
        assertEquals(scheduleDayOfWeek, changeTime.get(Calendar.DAY_OF_WEEK));
        assertEquals(15, changeTime.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void nextStreamURLChangeTimeBetweenFromTimeAndToTimeBeforeDayOfWeek() {
        int calendarDayOfWeek = Calendar.TUESDAY;
        int scheduleDayOfWeek = Calendar.FRIDAY;

        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_WEEK, calendarDayOfWeek);

        Streamurlschedule schedule = new Streamurlschedule(
                (byte) scheduleDayOfWeek, time1500, time2100);
        Calendar changeTime = StreamURLResource.nextStreamURLChangeTime(
                calendar, schedule);

        assertTrue(changeTime.after(calendar));
        assertEquals(scheduleDayOfWeek, changeTime.get(Calendar.DAY_OF_WEEK));
        assertEquals(15, changeTime.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void nextStreamURLChangeTimeAdterToTimeBeforeDayOfWeek() {
        int calendarDayOfWeek = Calendar.TUESDAY;
        int scheduleDayOfWeek = Calendar.FRIDAY;

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.DAY_OF_WEEK, calendarDayOfWeek);

        Streamurlschedule schedule = new Streamurlschedule(
                (byte) scheduleDayOfWeek, time1500, time2100);
        Calendar changeTime = StreamURLResource.nextStreamURLChangeTime(
                calendar, schedule);

        assertTrue(changeTime.after(calendar));
        assertEquals(scheduleDayOfWeek, changeTime.get(Calendar.DAY_OF_WEEK));
        assertEquals(15, changeTime.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void nextStreamURLChangeTimeBeforeFromTimeAfterDayOfWeek() {
        int calendarDayOfWeek = Calendar.FRIDAY;
        int scheduleDayOfWeek = Calendar.TUESDAY;

        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.DAY_OF_WEEK, calendarDayOfWeek);

        Streamurlschedule schedule = new Streamurlschedule(
                (byte) scheduleDayOfWeek, time1500, time2100);
        Calendar changeTime = StreamURLResource.nextStreamURLChangeTime(
                calendar, schedule);

        assertTrue(changeTime.after(calendar));
        assertEquals(scheduleDayOfWeek, changeTime.get(Calendar.DAY_OF_WEEK));
        assertEquals(15, changeTime.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void nextStreamURLChangeTimeBetweenFromTimeAndToTimeAfterDayOfWeek() {
        int calendarDayOfWeek = Calendar.FRIDAY;
        int scheduleDayOfWeek = Calendar.TUESDAY;

        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_WEEK, calendarDayOfWeek);

        Streamurlschedule schedule = new Streamurlschedule(
                (byte) scheduleDayOfWeek, time1500, time2100);
        Calendar changeTime = StreamURLResource.nextStreamURLChangeTime(
                calendar, schedule);

        assertTrue(changeTime.after(calendar));
        assertEquals(scheduleDayOfWeek, changeTime.get(Calendar.DAY_OF_WEEK));
        assertEquals(15, changeTime.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void nextStreamURLChangeTimeAfterToTimeAfterDayOfWeek() {
        int calendarDayOfWeek = Calendar.FRIDAY;
        int scheduleDayOfWeek = Calendar.TUESDAY;

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.DAY_OF_WEEK, calendarDayOfWeek);

        Streamurlschedule schedule = new Streamurlschedule(
                (byte) scheduleDayOfWeek, time1500, time2100);
        Calendar changeTime = StreamURLResource.nextStreamURLChangeTime(
                calendar, schedule);

        assertTrue(changeTime.after(calendar));
        assertEquals(scheduleDayOfWeek, changeTime.get(Calendar.DAY_OF_WEEK));
        assertEquals(15, changeTime.get(Calendar.HOUR_OF_DAY));
    }
}
