package no.srib.app.server.util;

import java.util.Calendar;

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
}
