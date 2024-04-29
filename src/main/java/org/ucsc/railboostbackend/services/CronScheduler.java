package org.ucsc.railboostbackend.services;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CronScheduler implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // called when the server is initialized.

        JobDetail job = JobBuilder.newJob(JourneyCreator.class)
                .withIdentity("create_journey")
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("journey")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 05 0 * * ?"))
                .build();

        try {
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();

            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            System.out.println("Error: Couldn't create Scheduler : JourneyScheduler.java (line: 29)");
            System.out.println(e.getMessage());
        }

        System.out.println("Job added to the schedule!");
    }
}
