package no.srib.app.server.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

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

    @Test
    public void setDayOfWeekAndTimeGoForwardLaterDayLaterTime() {
        int currentDay = Calendar.TUESDAY;
        int currentHour = 9;
        int currentMinute = 10;
        int currentSecond = 37;

        int expectedDay = Calendar.THURSDAY;
        int expectedHour = 15;
        int expectedMinute = 0;
        int expectedSecond = 0;

        now.set(Calendar.DAY_OF_WEEK, currentDay);
        now.set(Calendar.HOUR_OF_DAY, currentHour);
        now.set(Calendar.MINUTE, currentMinute);
        now.set(Calendar.SECOND, currentSecond);
        timeCalendar.set(Calendar.HOUR_OF_DAY, expectedHour);
        timeCalendar.set(Calendar.MINUTE, expectedMinute);
        timeCalendar.set(Calendar.SECOND, expectedSecond);

        Calendar actual = (Calendar) now.clone();
        java.sql.Time time = new java.sql.Time(timeCalendar.getTimeInMillis());
        TimeUtil.setDayOfWeekAndTimeGoForward(actual, expectedDay, time);

        assertTrue(actual.after(now));
        assertEquals(expectedDay, actual.get(Calendar.DAY_OF_WEEK));
        assertEquals(expectedHour, actual.get(Calendar.HOUR_OF_DAY));
        assertEquals(expectedMinute, actual.get(Calendar.MINUTE));
        assertEquals(expectedSecond, actual.get(Calendar.SECOND));
    }

    @Test
    public void setDayOfWeekAndTimeGoForwardLaterDaySameTime() {
        int currentDay = Calendar.TUESDAY;
        int expectedDay = Calendar.THURSDAY;
        int hour = 15;
        int minute = 0;
        int second = 0;

        now.set(Calendar.DAY_OF_WEEK, currentDay);
        now.set(Calendar.HOUR_OF_DAY, hour);
        now.set(Calendar.MINUTE, minute);
        now.set(Calendar.SECOND, second);
        timeCalendar.set(Calendar.HOUR_OF_DAY, hour);
        timeCalendar.set(Calendar.MINUTE, minute);
        timeCalendar.set(Calendar.SECOND, second);

        Calendar actual = (Calendar) now.clone();
        java.sql.Time time = new java.sql.Time(timeCalendar.getTimeInMillis());
        TimeUtil.setDayOfWeekAndTimeGoForward(actual, expectedDay, time);

        assertTrue(actual.after(now));
        assertEquals(expectedDay, actual.get(Calendar.DAY_OF_WEEK));
        assertEquals(hour, actual.get(Calendar.HOUR_OF_DAY));
        assertEquals(minute, actual.get(Calendar.MINUTE));
        assertEquals(second, actual.get(Calendar.SECOND));
    }

    @Test
    public void setDayOfWeekAndTimeGoForwardLaterDayEarlierTime() {
        int currentDay = Calendar.TUESDAY;
        int currentHour = 21;
        int currentMinute = 10;
        int currentSecond = 37;

        int expectedDay = Calendar.THURSDAY;
        int expectedHour = 15;
        int expectedMinute = 0;
        int expectedSecond = 0;

        now.set(Calendar.DAY_OF_WEEK, currentDay);
        now.set(Calendar.HOUR_OF_DAY, currentHour);
        now.set(Calendar.MINUTE, currentMinute);
        now.set(Calendar.SECOND, currentSecond);
        timeCalendar.set(Calendar.HOUR_OF_DAY, expectedHour);
        timeCalendar.set(Calendar.MINUTE, expectedMinute);
        timeCalendar.set(Calendar.SECOND, expectedSecond);

        Calendar actual = (Calendar) now.clone();
        java.sql.Time time = new java.sql.Time(timeCalendar.getTimeInMillis());
        TimeUtil.setDayOfWeekAndTimeGoForward(actual, expectedDay, time);

        assertTrue(actual.after(now));
        assertEquals(expectedDay, actual.get(Calendar.DAY_OF_WEEK));
        assertEquals(expectedHour, actual.get(Calendar.HOUR_OF_DAY));
        assertEquals(expectedMinute, actual.get(Calendar.MINUTE));
        assertEquals(expectedSecond, actual.get(Calendar.SECOND));
    }

    @Test
    public void setDayOfWeekAndTimeGoForwardSameDayLaterTime() {
        int day = Calendar.TUESDAY;

        int currentHour = 9;
        int currentMinute = 10;
        int currentSecond = 37;

        int expectedHour = 15;
        int expectedMinute = 0;
        int expectedSecond = 0;

        now.set(Calendar.DAY_OF_WEEK, day);
        now.set(Calendar.HOUR_OF_DAY, currentHour);
        now.set(Calendar.MINUTE, currentMinute);
        now.set(Calendar.SECOND, currentSecond);
        timeCalendar.set(Calendar.HOUR_OF_DAY, expectedHour);
        timeCalendar.set(Calendar.MINUTE, expectedMinute);
        timeCalendar.set(Calendar.SECOND, expectedSecond);

        Calendar actual = (Calendar) now.clone();
        java.sql.Time time = new java.sql.Time(timeCalendar.getTimeInMillis());
        TimeUtil.setDayOfWeekAndTimeGoForward(actual, day, time);

        assertTrue(actual.after(now));
        assertEquals(now.get(Calendar.YEAR), actual.get(Calendar.YEAR));
        assertEquals(now.get(Calendar.DAY_OF_YEAR),
                actual.get(Calendar.DAY_OF_YEAR));
        assertEquals(day, actual.get(Calendar.DAY_OF_WEEK));
        assertEquals(expectedHour, actual.get(Calendar.HOUR_OF_DAY));
        assertEquals(expectedMinute, actual.get(Calendar.MINUTE));
        assertEquals(expectedSecond, actual.get(Calendar.SECOND));
    }

    @Test
    public void setDayOfWeekAndTimeGoForwardSameDaySameTime() {
        int day = Calendar.TUESDAY;
        int hour = 15;
        int minute = 0;
        int second = 0;

        now.set(Calendar.DAY_OF_WEEK, day);
        now.set(Calendar.HOUR_OF_DAY, hour);
        now.set(Calendar.MINUTE, minute);
        now.set(Calendar.SECOND, second);
        now.set(Calendar.MILLISECOND, 0);
        timeCalendar.set(Calendar.HOUR_OF_DAY, hour);
        timeCalendar.set(Calendar.MINUTE, minute);
        timeCalendar.set(Calendar.SECOND, second);

        Calendar actual = (Calendar) now.clone();
        java.sql.Time time = new java.sql.Time(timeCalendar.getTimeInMillis());
        TimeUtil.setDayOfWeekAndTimeGoForward(actual, day, time);

        assertTrue(actual.after(now));
        assertEquals(7, TimeUtil.getTimeDiff(now, actual, TimeUnit.DAYS));
        assertEquals(day, actual.get(Calendar.DAY_OF_WEEK));
        assertEquals(hour, actual.get(Calendar.HOUR_OF_DAY));
        assertEquals(minute, actual.get(Calendar.MINUTE));
        assertEquals(second, actual.get(Calendar.SECOND));
    }

    @Test
    public void setDayOfWeekAndTimeGoForwardSameDayEarlierTime() {
        int day = Calendar.TUESDAY;

        int currentHour = 21;
        int currentMinute = 10;
        int currentSecond = 37;

        int expectedHour = 15;
        int expectedMinute = 0;
        int expectedSecond = 0;

        now.set(Calendar.DAY_OF_WEEK, day);
        now.set(Calendar.HOUR_OF_DAY, currentHour);
        now.set(Calendar.MINUTE, currentMinute);
        now.set(Calendar.SECOND, currentSecond);
        timeCalendar.set(Calendar.HOUR_OF_DAY, expectedHour);
        timeCalendar.set(Calendar.MINUTE, expectedMinute);
        timeCalendar.set(Calendar.SECOND, expectedSecond);

        Calendar actual = (Calendar) now.clone();
        java.sql.Time time = new java.sql.Time(timeCalendar.getTimeInMillis());
        TimeUtil.setDayOfWeekAndTimeGoForward(actual, day, time);

        assertTrue(actual.after(now));
        assertEquals(6, TimeUtil.getTimeDiff(now, actual, TimeUnit.DAYS));
        assertEquals(day, actual.get(Calendar.DAY_OF_WEEK));
        assertEquals(expectedHour, actual.get(Calendar.HOUR_OF_DAY));
        assertEquals(expectedMinute, actual.get(Calendar.MINUTE));
        assertEquals(expectedSecond, actual.get(Calendar.SECOND));
    }

    @Test
    public void setDayOfWeekAndTimeGoForwardEarlierDayLaterTime() {
        int currentDay = Calendar.FRIDAY;
        int currentHour = 9;
        int currentMinute = 10;
        int currentSecond = 37;

        int expectedDay = Calendar.MONDAY;
        int expectedHour = 15;
        int expectedMinute = 0;
        int expectedSecond = 0;

        now.set(Calendar.DAY_OF_WEEK, currentDay);
        now.set(Calendar.HOUR_OF_DAY, currentHour);
        now.set(Calendar.MINUTE, currentMinute);
        now.set(Calendar.SECOND, currentSecond);
        timeCalendar.set(Calendar.HOUR_OF_DAY, expectedHour);
        timeCalendar.set(Calendar.MINUTE, expectedMinute);
        timeCalendar.set(Calendar.SECOND, expectedSecond);

        Calendar actual = (Calendar) now.clone();
        java.sql.Time time = new java.sql.Time(timeCalendar.getTimeInMillis());
        TimeUtil.setDayOfWeekAndTimeGoForward(actual, expectedDay, time);

        assertTrue(actual.after(now));
        assertEquals(3, TimeUtil.getTimeDiff(now, actual, TimeUnit.DAYS));
        assertEquals(expectedDay, actual.get(Calendar.DAY_OF_WEEK));
        assertEquals(expectedHour, actual.get(Calendar.HOUR_OF_DAY));
        assertEquals(expectedMinute, actual.get(Calendar.MINUTE));
        assertEquals(expectedSecond, actual.get(Calendar.SECOND));
    }

    @Test
    public void setDayOfWeekAndTimeGoForwardEarlierDaySameTime() {
        int currentDay = Calendar.FRIDAY;
        int expectedDay = Calendar.MONDAY;
        int hour = 15;
        int minute = 0;
        int second = 0;

        now.set(Calendar.DAY_OF_WEEK, currentDay);
        now.set(Calendar.HOUR_OF_DAY, hour);
        now.set(Calendar.MINUTE, minute);
        now.set(Calendar.SECOND, second);
        now.set(Calendar.MILLISECOND, 0);
        timeCalendar.set(Calendar.HOUR_OF_DAY, hour);
        timeCalendar.set(Calendar.MINUTE, minute);
        timeCalendar.set(Calendar.SECOND, second);

        Calendar actual = (Calendar) now.clone();
        java.sql.Time time = new java.sql.Time(timeCalendar.getTimeInMillis());
        TimeUtil.setDayOfWeekAndTimeGoForward(actual, expectedDay, time);

        assertTrue(actual.after(now));
        assertEquals(3, TimeUtil.getTimeDiff(now, actual, TimeUnit.DAYS));
        assertEquals(expectedDay, actual.get(Calendar.DAY_OF_WEEK));
        assertEquals(hour, actual.get(Calendar.HOUR_OF_DAY));
        assertEquals(minute, actual.get(Calendar.MINUTE));
        assertEquals(second, actual.get(Calendar.SECOND));
    }

    @Test
    public void setDayOfWeekAndTimeGoForwardEarlierDayEarlierTime() {
        int currentDay = Calendar.FRIDAY;
        int currentHour = 21;
        int currentMinute = 10;
        int currentSecond = 37;

        int expectedDay = Calendar.MONDAY;
        int expectedHour = 15;
        int expectedMinute = 0;
        int expectedSecond = 0;

        now.set(Calendar.DAY_OF_WEEK, currentDay);
        now.set(Calendar.HOUR_OF_DAY, currentHour);
        now.set(Calendar.MINUTE, currentMinute);
        now.set(Calendar.SECOND, currentSecond);
        timeCalendar.set(Calendar.HOUR_OF_DAY, expectedHour);
        timeCalendar.set(Calendar.MINUTE, expectedMinute);
        timeCalendar.set(Calendar.SECOND, expectedSecond);

        Calendar actual = (Calendar) now.clone();
        java.sql.Time time = new java.sql.Time(timeCalendar.getTimeInMillis());
        TimeUtil.setDayOfWeekAndTimeGoForward(actual, expectedDay, time);

        assertTrue(actual.after(now));
        assertEquals(2, TimeUtil.getTimeDiff(now, actual, TimeUnit.DAYS));
        assertEquals(expectedDay, actual.get(Calendar.DAY_OF_WEEK));
        assertEquals(expectedHour, actual.get(Calendar.HOUR_OF_DAY));
        assertEquals(expectedMinute, actual.get(Calendar.MINUTE));
        assertEquals(expectedSecond, actual.get(Calendar.SECOND));
    }
}
