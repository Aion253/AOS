package net.aionstudios.api.cron;

import net.aionstudios.api.service.DateTimeServices;

/**
 * A class that helps to define timing for {@link CronJob}s.
 * @author Winter Roberts
 */
public class CronDateTime {
	
	private String cronTime = "* * * * * *";
	
	public CronDateTime() {
		
	}
	
	//Has to be assembled, not pre-provided because I don't want to do the string processing.
	
	/**
	 * Constructs a complete crontTime from the information provided.
	 * @param minute A minute or minute range provided in the form of a string (ints).
	 * @param hour An hour or hour range provided in the form of a string (ints).
	 * @param day A day or day range provided in the form of a string (ints).
	 * @param month A month or month range provided in the form of a string (ints).
	 * @param dow A day of the week or day of the week range provided in the form of a string (ints).
	 * @param year A year or year range provided in the form of a string (ints).
	 */
	private void constructCronString(String minute, String hour, String day, String month, String dow, String year) {
		cronTime = minute+" "+hour+" "+day+" "+month+" "+dow+" "+year;
	}
	
	/**
	 * @return A string array containing each cron definition in separate parts. m h d m w y
	 */
	private String[] getCronTimes() {
		return cronTime.split(" ");
	}
	
	/**
	 * Sets the exact or range of minutes for which this {@link CronDateTime} will permit a {@link CronJob} to execute.
	 * A note: start must always be less than or equal to end.
	 * @param start	An integer representing the start of valid minutes (0-59)
	 * @param end An integer representing the end of valid minutes (0-59)
	 * @return True if all inputs were valid and the changes were applied, false otherwise.
	 */
	public boolean setMinuteRange(int start, int end) {
		if(start>=0&&start<60&&end>=start&&end<60) {
			String[] current = getCronTimes();
			if(start!=end) {
				constructCronString(start+"-"+end, current[1], current[2], current[3], current[4], current[5]);
			} else {
				constructCronString(start+"", current[1], current[2], current[3], current[4], current[5]);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Sets the exact or range of hours for which this {@link CronDateTime} will permit a {@link CronJob} to execute.
	 * A note: start must always be less than or equal to end.
	 * @param start	An integer representing the start of valid hours (0-23)
	 * @param end An integer representing the end of valid hours (0-23)
	 * @return True if all inputs were valid and the changes were applied, false otherwise.
	 */
	public boolean setHourRange(int start, int end) {
		if(start>=0&&start<24&&end>=start&&end<24) {
			String[] current = getCronTimes();
			if(start!=end) {
				constructCronString(current[0], start+"-"+end, current[2], current[3], current[4], current[5]);
			} else {
				constructCronString(current[0], start+"", current[2], current[3], current[4], current[5]);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Sets the exact or range of days of the month for which this {@link CronDateTime} will permit a {@link CronJob} to execute.
	 * A note: start must always be less than or equal to end.
	 * @param start	An integer representing the start of valid days (0-31)
	 * @param end An integer representing the end of valid days (0-31)
	 * @return True if all inputs were valid and the changes were applied, false otherwise.
	 */
	public boolean setDayOfMonthRange(int start, int end) {
		if(start>0&&start<32&&end>=start&&end<32) {
			String[] current = getCronTimes();
			if(start!=end) {
				constructCronString(current[0], current[1], start+"-"+end, current[3], current[4], current[5]);
			} else {
				constructCronString(current[0], current[1], start+"", current[3], current[4], current[5]);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Sets the exact or range of months for which this {@link CronDateTime} will permit a {@link CronJob} to execute.
	 * A note: start must always be less than or equal to end.
	 * @param start	An integer representing the start of valid months (1-12)
	 * @param end An integer representing the end of valid months (1-12)
	 * @return True if all inputs were valid and the changes were applied, false otherwise.
	 */
	public boolean setMonthRange(int start, int end) {
		if(start>0&&start<13&&end>=start&&end<13) {
			String[] current = getCronTimes();
			if(start!=end) {
				constructCronString(current[0], current[1], current[2], start+"-"+end, current[4], current[5]);
			} else {
				constructCronString(current[0], current[1], current[2], start+"", current[4], current[5]);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Sets the exact or range of days of the week for which this {@link CronDateTime} will permit a {@link CronJob} to execute.
	 * A note: start must always be less than or equal to end.
	 * @param start	An integer representing the start of valid days (1-7)
	 * @param end An integer representing the end of valid days (1-7)
	 * @return True if all inputs were valid and the changes were applied, false otherwise.
	 */
	public boolean setDayOfWeekRange(int start, int end) {
		if(start>0&&start<8&&end>=start&&end<8) {
			String[] current = getCronTimes();
			if(start!=end) {
				constructCronString(current[0], current[1], current[2], current[3], start+"-"+end, current[5]);
			} else {
				constructCronString(current[0], current[1], current[2], current[3], start+"", current[5]);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Sets the exact or range of years for which this {@link CronDateTime} will permit a {@link CronJob} to execute.
	 * A note: start must always be less than or equal to end.
	 * @param start	An integer representing the start of valid years (1900-2999)
	 * @param end An integer representing the end of valid hours (1900-2999)
	 * @return True if all inputs were valid and the changes were applied, false otherwise.
	 */
	public boolean setYearRange(int start, int end) {
		if(start>1899&&start<3000&&end>=start&&end<3001) {
			String[] current = getCronTimes();
			if(start!=end) {
				constructCronString(current[0], current[1], current[2], current[3], current[4], start+"-"+end);
			} else {
				constructCronString(current[0], current[1], current[2], current[3], current[4], start+"");
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Sets as that this {@link CronDateTime} will permit a {@link CronJob} to execute for every minute.
	 */
	public void setAllMinuteRange() {
		String[] current = getCronTimes();
		constructCronString("*", current[1], current[2], current[3], current[4], current[5]);
	}
	
	/**
	 * Sets as that this {@link CronDateTime} will permit a {@link CronJob} to execute for every hour.
	 */
	public void setAllHourRange() {
		String[] current = getCronTimes();
		constructCronString(current[0], "*", current[2], current[3], current[4], current[5]);
	}
	
	/**
	 * Sets as that this {@link CronDateTime} will permit a {@link CronJob} to execute every for day of the month.
	 */
	public void setAllDayOfMonthRange() {
		String[] current = getCronTimes();
		constructCronString(current[0], current[1], "*", current[3], current[4], current[5]);
	}
	
	/**
	 * Sets as that this {@link CronDateTime} will permit a {@link CronJob} to execute every for month.
	 */
	public void setAllMonthRange() {
		String[] current = getCronTimes();
		constructCronString(current[0], current[1], current[2], "*", current[4], current[5]);
	}
	
	/**
	 * Sets as that this {@link CronDateTime} will permit a {@link CronJob} to execute every day of the week.
	 */
	public void setAllDayOfWeekRange() {
		String[] current = getCronTimes();
		constructCronString(current[0], current[1], current[2], current[3], "*", current[5]);
	}
	
	/**
	 * Sets as that this {@link CronDateTime} will permit a {@link CronJob} to execute every year.
	 */
	public void setAllYearRange() {
		String[] current = getCronTimes();
		constructCronString(current[0], current[1], current[2], current[3], current[4], "*");
	}
	
	/**
	 * Appends an exact or range of minutes for which this {@link CronDateTime} will permit a {@link CronJob} to execute.
	 * A note: start must always be less than or equal to end.
	 * @param start	An integer representing the start of valid minutes (0-59)
	 * @param end An integer representing the end of valid minutes (0-59)
	 * @return True if all inputs were valid and the changes were applied, false otherwise.
	 */
	public boolean appendMinuteRange(int start, int end) {
		if(start>=0&&start<60&&end>=start&&end<60) {
			String[] current = getCronTimes();
			if(current[0].equals("*")) {
				return setMinuteRange(start, end);
			}
			if(start!=end) {
				constructCronString(current[0]+","+start+"-"+end, current[1], current[2], current[3], current[4], current[5]);
			} else {
				constructCronString(current[0]+","+start+"", current[1], current[2], current[3], current[4], current[5]);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Appends an exact or range of hours for which this {@link CronDateTime} will permit a {@link CronJob} to execute.
	 * A note: start must always be less than or equal to end.
	 * @param start	An integer representing the start of valid hours (0-23)
	 * @param end An integer representing the end of valid hours (0-23)
	 * @return True if all inputs were valid and the changes were applied, false otherwise.
	 */
	public boolean appendHourRange(int start, int end) {
		if(start>=0&&start<24&&end>=start&&end<24) {
			String[] current = getCronTimes();
			if(current[1].equals("*")) {
				return setHourRange(start, end);
			}
			if(start!=end) {
				constructCronString(current[0], current[1]+","+start+"-"+end, current[2], current[3], current[4], current[5]);
			} else {
				constructCronString(current[0], current[1]+","+start+"", current[2], current[3], current[4], current[5]);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Appends an exact or range of days of the month for which this {@link CronDateTime} will permit a {@link CronJob} to execute.
	 * A note: start must always be less than or equal to end.
	 * @param start	An integer representing the start of valid days (0-31)
	 * @param end An integer representing the end of valid days (0-31)
	 * @return True if all inputs were valid and the changes were applied, false otherwise.
	 */
	public boolean appendDayOfMonthRange(int start, int end) {
		if(start>0&&start<32&&end>=start&&end<32) {
			String[] current = getCronTimes();
			if(current[2].equals("*")) {
				return setDayOfMonthRange(start, end);
			}
			if(start!=end) {
				constructCronString(current[0], current[1], current[2]+","+start+"-"+end, current[3], current[4], current[5]);
			} else {
				constructCronString(current[0], current[1], current[2]+","+start+"", current[3], current[4], current[5]);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Appends an exact or range of months for which this {@link CronDateTime} will permit a {@link CronJob} to execute.
	 * A note: start must always be less than or equal to end.
	 * @param start	An integer representing the start of valid months (1-12)
	 * @param end An integer representing the end of valid months (1-12)
	 * @return True if all inputs were valid and the changes were applied, false otherwise.
	 */
	public boolean appendMonthRange(int start, int end) {
		if(start>0&&start<13&&end>=start&&end<13) {
			String[] current = getCronTimes();
			if(current[3].equals("*")) {
				return setMonthRange(start, end);
			}
			if(start!=end) {
				constructCronString(current[0], current[1], current[2], current[3]+","+start+"-"+end, current[4], current[5]);
			} else {
				constructCronString(current[0], current[1], current[2], current[3]+","+start+"", current[4], current[5]);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Appends an exact or range of days of the week for which this {@link CronDateTime} will permit a {@link CronJob} to execute.
	 * A note: start must always be less than or equal to end.
	 * @param start	An integer representing the start of valid days (1-7)
	 * @param end An integer representing the end of valid days (1-7)
	 * @return True if all inputs were valid and the changes were applied, false otherwise.
	 */
	public boolean appendDayOfWeekRange(int start, int end) {
		if(start>0&&start<8&&end>=start&&end<8) {
			String[] current = getCronTimes();
			if(current[4].equals("*")) {
				return setDayOfWeekRange(start, end);
			}
			if(start!=end) {
				constructCronString(current[0], current[1], current[2], current[3], current[4]+","+start+"-"+end, current[5]);
			} else {
				constructCronString(current[0], current[1], current[2], current[3], current[4]+","+start+"", current[5]);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Appends an exact or range of years for which this {@link CronDateTime} will permit a {@link CronJob} to execute.
	 * A note: start must always be less than or equal to end.
	 * @param start	An integer representing the start of valid years (1900-2999)
	 * @param end An integer representing the end of valid hours (1900-2999)
	 * @return True if all inputs were valid and the changes were applied, false otherwise.
	 */
	public boolean appendYearRange(int start, int end) {
		if(start>1899&&start<3000&&end>=start&&end<3001) {
			String[] current = getCronTimes();
			if(current[5].equals("*")) {
				return setYearRange(start, end);
			}
			if(start!=end) {
				constructCronString(current[0], current[1], current[2], current[3], current[4], current[5]+","+start+"-"+end);
			} else {
				constructCronString(current[0], current[1], current[2], current[3], current[4], current[5]+","+start+"");
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Detects whether or not the {@link CronDateTime} supports an integer for a given minute.
	 * @param match An integer to match against.
	 * @return True if the {@link CronDateTime} supports the provided integer.
	 */
	public boolean hasMinute(int match) {
		String[] current = getCronTimes();
		if(current[0].contains("*")) {
			return true;
		}
		for(String s1 : current[0].split(",")) {
			String[] s2 = s1.split("-");
			if(s2.length>1) {
				if(Integer.parseInt(s2[0])<=match&&Integer.parseInt(s2[1])>=match) {
					return true;
				}
			} else {
				if(Integer.parseInt(s2[0])==match) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Detects whether or not the {@link CronDateTime} supports an integer for a given hour.
	 * @param match An integer to match against.
	 * @return True if the {@link CronDateTime} supports the provided integer.
	 */
	public boolean hasHour(int match) {
		String[] current = getCronTimes();
		if(current[1].contains("*")) {
			return true;
		}
		for(String s1 : current[1].split(",")) {
			String[] s2 = s1.split("-");
			if(s2.length>1) {
				if(Integer.parseInt(s2[0])<=match&&Integer.parseInt(s2[1])>=match) {
					return true;
				}
			} else {
				if(Integer.parseInt(s2[0])==match) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Detects whether or not the {@link CronDateTime} supports an integer for a given day of the month.
	 * @param match An integer to match against.
	 * @return True if the {@link CronDateTime} supports the provided integer.
	 */
	public boolean hasDayOfMonth(int match) {
		String[] current = getCronTimes();
		if(current[2].contains("*")) {
			return true;
		}
		for(String s1 : current[2].split(",")) {
			String[] s2 = s1.split("-");
			if(s2.length>1) {
				if(Integer.parseInt(s2[0])<=match&&Integer.parseInt(s2[1])>=match) {
					return true;
				}
			} else {
				if(Integer.parseInt(s2[0])==match) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Detects whether or not the {@link CronDateTime} supports an integer for a given month.
	 * @param match An integer to match against.
	 * @return True if the {@link CronDateTime} supports the provided integer.
	 */
	public boolean hasMonth(int match) {
		String[] current = getCronTimes();
		if(current[3].contains("*")) {
			return true;
		}
		for(String s1 : current[3].split(",")) {
			String[] s2 = s1.split("-");
			if(s2.length>1) {
				if(Integer.parseInt(s2[0])<=match&&Integer.parseInt(s2[1])>=match) {
					return true;
				}
			} else {
				if(Integer.parseInt(s2[0])==match) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Detects whether or not the {@link CronDateTime} supports an integer for a given day of the week.
	 * @param match An integer to match against.
	 * @return True if the {@link CronDateTime} supports the provided integer.
	 */
	public boolean hasDayOfWeek(int match) {
		String[] current = getCronTimes();
		if(current[4].contains("*")) {
			return true;
		}
		for(String s1 : current[4].split(",")) {
			String[] s2 = s1.split("-");
			if(s2.length>1) {
				if(Integer.parseInt(s2[0])<=match&&Integer.parseInt(s2[1])>=match) {
					return true;
				}
			} else {
				if(Integer.parseInt(s2[0])==match) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Detects whether or not the {@link CronDateTime} supports an integer for a given year.
	 * @param match An integer to match against.
	 * @return True if the {@link CronDateTime} supports the provided integer.
	 */
	public boolean hasYear(int match) {
		String[] current = getCronTimes();
		if(current[5].contains("*")) {
			return true;
		}
		for(String s1 : current[5].split(",")) {
			String[] s2 = s1.split("-");
			if(s2.length>1) {
				if(Integer.parseInt(s2[0])<=match&&Integer.parseInt(s2[1])>=match) {
					return true;
				}
			} else {
				if(Integer.parseInt(s2[0])==match) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks this {@link CronDateTime} against the current time.
	 * @return True if the present system time matches with the sets in this {@link CronDateTime}, false otherwise.
	 */
	public boolean matchesNow() {
		return hasMinute(DateTimeServices.getCronMinute())&&hasHour(DateTimeServices.getCronHour())&&hasDayOfMonth(DateTimeServices.getCronDayOfMonth())&&hasMonth(DateTimeServices.getCronMonth())&&hasDayOfWeek(DateTimeServices.getCronDayOfWeek())&&hasYear(DateTimeServices.getCronYear());
	}
	
	/**
	 * Checks this {@link CronDateTime} against a provided time.
	 * @param min The minute to check against.
	 * @param hour The hour to check against.
	 * @param dom The day of the month to check against.
	 * @param month The month to check against.
	 * @param dow The day of the week to check against.
	 * @param year The year to check against.
	 * @return True if the provided system time matches with the sets in this {@link CronDateTime}, false otherwise.
	 */
	public boolean matches(int min, int hour, int dom, int month, int dow, int year) {
		return hasMinute(min)&&hasHour(hour)&&hasDayOfMonth(dom)&&hasMonth(month)&&hasDayOfWeek(dow)&&hasYear(year);
	}

}

