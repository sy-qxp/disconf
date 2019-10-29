package cn.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyQuartzJob implements Job {
    Logger log = LoggerFactory.getLogger(MyQuartzJob.class);
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            log.info("==================:"+this.getClass().getName() + " was just triggered...");
    }
}
