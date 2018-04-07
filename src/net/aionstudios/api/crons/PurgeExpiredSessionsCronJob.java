package net.aionstudios.api.crons;

import net.aionstudios.api.cron.CronDateTime;
import net.aionstudios.api.cron.CronJob;
import net.aionstudios.api.service.AccountServices;

public class PurgeExpiredSessionsCronJob extends CronJob {

	public PurgeExpiredSessionsCronJob() {
		super(new CronDateTime());
	}

	@Override
	public void run() {
		AccountServices.purgeExpiredSessions();
	}

}
