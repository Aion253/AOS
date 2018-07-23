package net.aionstudios.api.crons;

import net.aionstudios.api.context.Context;
import net.aionstudios.api.cron.CronDateTime;
import net.aionstudios.api.cron.CronJob;
import net.aionstudios.api.service.AccountServices;

/**
 * An AOS-provided {@link CronJob} for removing old client-authenticated sessions from the APIs database.
 * 
 * @author Winter Roberts
 *
 */
public class PurgeExpiredSessionsCronJob extends CronJob {

	public PurgeExpiredSessionsCronJob() {
		super(new CronDateTime());
	}

	@Override
	public void run() {
		AccountServices.purgeExpiredSessions();
	}

}
