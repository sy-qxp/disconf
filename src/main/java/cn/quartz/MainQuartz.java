package cn.quartz;

import java.util.concurrent.TimeUnit;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainQuartz {
    private static Logger logger = LoggerFactory.getLogger(TestCronTrriger.class);

    public static void main(String[] args) {

        try {
            // 获取Scheduler实例
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            // 具体任务
            JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity("SimpleJob", "Job-Group-Simple").build();
            // 触发时间点
            SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("SimpleTrigger", "Trriger-Group-Simple").
                    startNow().withSchedule(simpleScheduleBuilder).build();
            // 交由Scheduler安排触发
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
            try {
                TimeUnit.MINUTES.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 关闭Scheduler
            scheduler.shutdown();
        } catch (SchedulerException se) {
            logger.error(se.getMessage(), se);
        }
    }

}