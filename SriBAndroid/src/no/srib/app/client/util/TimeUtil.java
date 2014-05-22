package no.srib.app.client.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeUtil {

	public static Calendar parseIntDate(int time) {

		String timeString = String.valueOf(time);
		int year = Integer.parseInt(timeString.substring(0, 4));
		// Months starts on 0 in GregorianCalander
		int month = Integer.valueOf(timeString.substring(4, 6)) - 1;
		int date = Integer.valueOf(timeString.substring(6, 8));
		Calendar cal = new GregorianCalendar(year, month, date);

		return cal;
	}
}
