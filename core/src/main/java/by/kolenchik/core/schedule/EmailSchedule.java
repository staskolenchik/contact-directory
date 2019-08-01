package by.kolenchik.core.schedule;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class EmailSchedule {

    private static Logger log = Logger.getLogger(EmailSchedule.class);


    protected void sendEmail(String email, String subject, String text) {

        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            scheduler.start();

            JobDetail jobAtStart = newJob(SendEmailJob.class)
                    .withIdentity("jobNotifyByEmailAtStart", "emailStart")
                    .usingJobData("email", email)
                    .usingJobData("subject", subject)
                    .usingJobData("text", text)
                    .build();

            JobDetail jobAtRepeat = newJob(SendEmailJob.class)
                    .withIdentity("jobNotifyByEmail", "emailRepeat")
                    .build();

            Trigger triggerAtStart = newTrigger()
                    .withIdentity("triggerNotifyByEmailAtStart", "emailStart")
                    .usingJobData("email", email)
                    .usingJobData("subject", subject)
                    .usingJobData("text", text)
                    .startNow()

                    .build();


            Trigger triggerAtRepeat = newTrigger()
                    .withIdentity("triggerNotifyByEmail", "emailRepeat")
                    .withSchedule(cronSchedule("0 0 0 * * ?"))
                    .build();

            scheduler.scheduleJob(jobAtStart, triggerAtStart);
            scheduler.scheduleJob(jobAtRepeat, triggerAtRepeat);



        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
    }
}
