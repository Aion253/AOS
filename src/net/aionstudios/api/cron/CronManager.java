package net.aionstudios.api.cron;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.aionstudios.api.server.APIServer;
import net.aionstudios.api.context.Context;
import net.aionstudios.api.context.ContextHandler;
import net.aionstudios.api.service.DateTimeServices;

/**
 * A class that accumulates {@link CronJob}s on the {@link APIServer} so that they can be executed by the cron thread on a schedule as defined by their associated {@link CronDateTime}s.
 * 
 * @author Winter Roberts
 *
 */
public class CronManager {
	
	private static List<CronJob> jobs = new ArrayList<CronJob>();
	private static boolean cronStarted = false;
	private static Thread cronThread;
	private static ExecutorService jobExecutor;
	
	/**
	 * Starts the cron thread and periodically checks if each enabled {@link CronJob} should be executed.
	 */
	public static void startCron() {
		if(!cronStarted) {
			cronStarted = true;
			jobExecutor = Executors.newCachedThreadPool();
			cronThread = new Thread() {
			    public void run() {
			    	int min = DateTimeServices.getCronMinute()-1;
			    	int hour, dom, month, dow, year = 0;
			    	while(cronStarted) {
			    		if(min!=DateTimeServices.getCronMinute()) {
			    			min = DateTimeServices.getCronMinute();
			    			hour = DateTimeServices.getCronHour();
			    			dom = DateTimeServices.getCronDayOfMonth();
			    			month = DateTimeServices.getCronMonth();
			    			dow = DateTimeServices.getCronDayOfWeek();
			    			year = DateTimeServices.getCronYear();
			    			for(CronJob j : jobs) {
			    				if(j.cronMatches(min, hour, dom, month, dow, year)) {
			    					jobExecutor.submit(new Runnable() {
										@Override public void run() {j.start();}
			    					});
			    				}
			    			}
			    		}
			    		try {
				            Thread.sleep(1000);
				        } catch(InterruptedException e) {
				            System.err.println("Cron thread was interrupted!");
				            e.printStackTrace();
				        }
			    	}
			    }  
			};

			cronThread.start();
		}
	}
	
	public static void addJob(CronJob j) {
		jobs.add(j);
	}

}
