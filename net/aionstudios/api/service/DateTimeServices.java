package net.aionstudios.api.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeServices {
	
	private static SimpleDateFormat dateform = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Calendar c;
	
	/**
	 * Generates a DateTime that matches the current time, compatible with MYSQL databases
	 * @return	The MYSQL formatted date
	 */
	public static String getMysqlCompatibleDateTime() {
		return dateform.format(new Date());
	}
	
	/**
	 * Gets the current datetime plus thirty minutes
	 * @return	The current time plus thirty minutes
	 */
	public static String getThirtyAddedDT() {
		long millis = Calendar.getInstance().getTimeInMillis();
		return dateform.format(new Date(millis+1800000));
	}
	
	public static String getSecondsAddedDT(long seconds) {
		long millis = Calendar.getInstance().getTimeInMillis();
		return dateform.format(new Date(millis+(1000*seconds)));
	}
	
	/**
	 * Reads a MYSQL formatted datetime to a Java date
	 * @param dateTime	A String representing the date and time in the form of yyyy-MM-dd HH:mm:ss
	 * @return	The given datetime string as a date object
	 */
	public static Date getDateTimeFromString(String dateTime) {
		Date d = new Date();
		try {
			d = dateform.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
	
	public static long getUnixTimestamp() {
		return new Date().getTime()/1000;
	}
	
	public static int getCronMinute() {
		c = Calendar.getInstance();
		return c.get(Calendar.MINUTE);
	}
	
	public static int getCronHour() {
		c = Calendar.getInstance();
		return c.get(Calendar.HOUR_OF_DAY)-1;
	}
	
	public static int getCronDayOfMonth() {
		c = Calendar.getInstance();
		return c.get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getCronMonth() {
		c = Calendar.getInstance();
		return c.get(Calendar.MONTH)+1;
	}
	
	public static int getCronDayOfWeek() {
		c = Calendar.getInstance();
		int i = c.get(Calendar.DAY_OF_WEEK)-1;
		return i<1 ? 7 : i;
	}
	
	public static int getCronYear() {
		c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}
	
	public static String getCronNowString() {
		return getCronMinute()+" "+getCronHour()+" "+getCronDayOfMonth()+" "+getCronMonth()+" "+getCronDayOfWeek()+" "+getCronYear();
	}

}
