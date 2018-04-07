package net.aionstudios.api.cron;

import net.aionstudios.api.service.DateTimeServices;

public class CronDateTime {
	
	private String cronTime = "* * * * * *";
	
	public CronDateTime() {
		
	}
	
	public CronDateTime(String cronTime) {
		
	}
	
	private void constructCronString(String minute, String hour, String day, String month, String dow, String year) {
		cronTime = minute+" "+hour+" "+day+" "+month+" "+dow+" "+year;
	}
	
	private String[] getCronTimes() {
		return cronTime.split(" ");
	}
	
	public boolean setMinuteRange(int start, int end) {
		if(start>=0&&start<60&&end>start&&end<60) {
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
	
	public boolean setHourRange(int start, int end) {
		if(start>=0&&start<24&&end>start&&end<24) {
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
	
	public boolean setDayOfMonthRange(int start, int end) {
		if(start>0&&start<32&&end>start&&end<32) {
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
	
	public boolean setMonthRange(int start, int end) {
		if(start>0&&start<13&&end>start&&end<13) {
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
	
	public boolean setDayOfWeekRange(int start, int end) {
		if(start>0&&start<8&&end>start&&end<8) {
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
	
	public boolean setYearRange(int start, int end) {
		if(start>1899&&start<3000&&end>start&&end<3001) {
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
	
	public boolean setAllMinuteRange() {
		String[] current = getCronTimes();
		constructCronString("*", current[1], current[2], current[3], current[4], current[5]);
		return true;
	}
	
	public boolean setAllHourRange() {
		String[] current = getCronTimes();
		constructCronString(current[0], "*", current[2], current[3], current[4], current[5]);
		return true;
	}
	
	public boolean setAllDayOfMonthRange() {
		String[] current = getCronTimes();
		constructCronString(current[0], current[1], "*", current[3], current[4], current[5]);
		return true;
	}
	
	public boolean setAllMonthRange() {
		String[] current = getCronTimes();
		constructCronString(current[0], current[1], current[2], "*", current[4], current[5]);
		return true;
	}
	
	public boolean setAllDayOfWeekRange() {
		String[] current = getCronTimes();
		constructCronString(current[0], current[1], current[2], current[3], "*", current[5]);
		return true;
	}
	
	public boolean setAllYearRange() {
		String[] current = getCronTimes();
		constructCronString(current[0], current[1], current[2], current[3], current[4], "*");
		return true;
	}
	
	public boolean appendMinuteRange(int start, int end) {
		if(start>=0&&start<60&&end>start&&end<60) {
			String[] current = getCronTimes();
			if(current[0]=="*") {
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
	
	public boolean appendHourRange(int start, int end) {
		if(start>=0&&start<24&&end>start&&end<24) {
			String[] current = getCronTimes();
			if(current[1]=="*") {
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
	
	public boolean appendDayOfMonthRange(int start, int end) {
		if(start>0&&start<32&&end>start&&end<32) {
			String[] current = getCronTimes();
			if(current[2]=="*") {
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
	
	public boolean appendMonthRange(int start, int end) {
		if(start>0&&start<13&&end>start&&end<13) {
			String[] current = getCronTimes();
			if(current[3]=="*") {
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
	
	public boolean appendDayOfWeekRange(int start, int end) {
		if(start>0&&start<8&&end>start&&end<8) {
			String[] current = getCronTimes();
			if(current[4]=="*") {
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
	
	public boolean appendYearRange(int start, int end) {
		if(start>1899&&start<3000&&end>start&&end<3001) {
			String[] current = getCronTimes();
			if(current[5]=="*") {
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
	
	public boolean matchesNow() {
		return hasMinute(DateTimeServices.getCronMinute())&&hasHour(DateTimeServices.getCronHour())&&hasDayOfMonth(DateTimeServices.getCronDayOfMonth())&&hasMonth(DateTimeServices.getCronMonth())&&hasDayOfWeek(DateTimeServices.getCronDayOfWeek())&&hasYear(DateTimeServices.getCronYear());
	}
	
	public boolean matches(int min, int hour, int dom, int month, int dow, int year) {
		return hasMinute(min)&&hasHour(hour)&&hasDayOfMonth(dom)&&hasMonth(month)&&hasDayOfWeek(dow)&&hasYear(year);
	}

}
