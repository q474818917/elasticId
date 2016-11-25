package io.sequence.elastic;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class ElasticRunnable implements Runnable{

	@Override
	public void run() {
		try {
            // Grab the Scheduler instance from the Factory
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            // and start it off
            scheduler.start();
            
            JobDetail job = JobBuilder.newJob(ElasticJob.class)
            	      .withIdentity("sequenceJob", "elastic")
            	      .build();

            	  // Trigger the job to run now, and then repeat every 40 seconds
            	  Trigger trigger = TriggerBuilder.newTrigger()
            	      .withIdentity("trigger", "elastic")
            	      .startNow()
            	            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))            
            	      .build();

            	  // Tell quartz to schedule the job using our trigger
            	  scheduler.scheduleJob(job, trigger);
            

        } catch (SchedulerException se) {
            se.printStackTrace();
        }
	}

}
