package net.aionstudios.api.cron;

public abstract class CronJob {
	
	private CronDateTime cdt;
	private boolean enabled = true;
	
	public CronJob(CronDateTime cdt) {
		this.cdt = cdt;
	}
	
	public void setCronDateTime(CronDateTime cdt) {
		this.cdt = cdt;
	}
	
	public CronDateTime getCronDateTime() {
		return cdt;
	}
	
	public boolean cronMatchesNow() {
		return cdt.matchesNow();
	}
	
	public boolean cronMatches(int min, int hour, int dom, int month, int dow, int year) {
		return cdt.matches(min, hour, dom, month, dow, year);
	}
	
	public void start() {
		run();
	}
	
	public abstract void run();
	
	public void enable() {
		enabled = true;
	}
	
	public void disable() {
		enabled = false;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

}
