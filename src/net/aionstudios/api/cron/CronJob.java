package net.aionstudios.api.cron;

/**
 * A class containing a method to be run at times specified by a {@link CronDateTime}.
 * 
 * @author Winter Roberts
 *
 */
public abstract class CronJob {
	
	private CronDateTime cdt;
	private boolean enabled = true;
	
	/**
	 * Creates a new {@link CronJob}.
	 * 
	 * @param cdt The {@link CronDateTime} to define scheduling for the execution of this {@link CronJob}.
	 */
	public CronJob(CronDateTime cdt) {
		this.cdt = cdt;
	}
	
	/**
	 * Sets the {@link CronDateTime} of this {@link CronJob}
	 * 
	 * @param cdt The {@link CronDateTime} to define scheduling for the execution of this {@link CronJob}.
	 */
	public void setCronDateTime(CronDateTime cdt) {
		this.cdt = cdt;
	}
	
	/**
	 * @return The {@link CronDateTime} that defines scheduling for the execution of this {@link CronJob}.
	 */
	public CronDateTime getCronDateTime() {
		return cdt;
	}
	
	/**
	 * @return True if the {@link CronDateTime} that defines scheduling for the execution of this {@link CronJob} matches the current system time, false otherwise.
	 */
	public boolean cronMatchesNow() {
		return cdt.matchesNow();
	}
	
	/**
	 * @param min The minute to check against.
	 * @param hour The hour to check against.
	 * @param dom The day of the month to check against.
	 * @param month The month to check against.
	 * @param dow The day of the week to check against.
	 * @param year The year to check against.
	 * @return True if the provided system time matches with the sets in the {@link CronDateTime} that defines scheduling for the execution of this {@link CronJob}, false otherwise.
	 */
	public boolean cronMatches(int min, int hour, int dom, int month, int dow, int year) {
		return cdt.matches(min, hour, dom, month, dow, year);
	}
	
	/**
	 * Starts this {@link CronJob} so that it can execute on the schedule as defined by its associated {@link CronDateTime}.
	 */
	public void start() {
		run();
	}
	
	/**
	 * A method that is called in enabled {@link CronJob}s when the cron thread determines that the present system time warrants execution of this {@link CronJob}.
	 */
	public abstract void run();
	
	/**
	 * Enables this {@link CronJob}.
	 */
	public void enable() {
		enabled = true;
	}
	
	/**
	 * Disables this {@link CronJob}.
	 */
	public void disable() {
		enabled = false;
	}
	
	/**
	 * @return True if this {@link CronJob} is enabled, false otherwise.
	 */
	public boolean isEnabled() {
		return enabled;
	}

}
