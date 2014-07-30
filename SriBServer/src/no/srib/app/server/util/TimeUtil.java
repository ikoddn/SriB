package no.srib.app.server.util;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

    public static int compare(final Calendar calendar, final java.sql.Time time) {
        Calendar timeCalendar = (Calendar) calendar.clone();
        timeCalendar.setTimeInMillis(time.getTime());

        int hourDiff = calendar.get(Calendar.HOUR_OF_DAY)
                - timeCalendar.get(Calendar.HOUR_OF_DAY);

        if (hourDiff == 0) {
            int minuteDiff = calendar.get(Calendar.MINUTE)
                    - timeCalendar.get(Calendar.MINUTE);

            if (minuteDiff == 0) {
                return calendar.get(Calendar.SECOND)
                        - timeCalendar.get(Calendar.SECOND);
            }

            return minuteDiff;
        }

        return hourDiff;
    }

    public static long getTimeDiff(final Calendar c1, final Calendar c2,
            final TimeUnit timeUnit) {

        return getTimeDiff(c1.getTimeInMillis(), c2.getTimeInMillis(), timeUnit);
    }

    public static long getTimeDiff(final long time1, final long time2,
            final TimeUnit timeUnit) {

        long diffInMillis = time2 - time1;
        return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public static void setDayOfWeekAndTimeGoForward(final Calendar calendar,
            final int dayOfWeek, final java.sql.Time time) {

        Calendar timeCalendar = (Calendar) calendar.clone();
        timeCalendar.setTime(time);

        int compareValue = compare(calendar, time);
        int dayDiff = dayOfWeek - calendar.get(Calendar.DAY_OF_WEEK);

        if (dayDiff < 0 || (dayDiff == 0 && compareValue >= 0)) {
            dayDiff += 7;
        }

        if (dayDiff > 0) {
            calendar.add(Calendar.DAY_OF_YEAR, dayDiff);
        }

        calendar.set(Calendar.HOUR_OF_DAY,
                timeCalendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
