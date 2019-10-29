package cn.quartz;

import java.util.concurrent.TimeUnit;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestCronTrriger {
    private static Logger logger = LoggerFactory.getLogger(TestCronTrriger.class);

    public static void main(String[] args) {

        try {
            // 获取Scheduler实例
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler cronScheduler = schedulerFactory.getScheduler();
            // 具体任务
            JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity("SimpleJob", "Job-Group-Simple").build();
            // 触发时间点
            // 交由Scheduler安排触发
            String cronExpression = "0 19 17 * * ? 2019"; // 2019年每天17点17分触发
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("SimpleTrigger", "Trriger-Group-Simple")
                    .withSchedule(cronScheduleBuilder)
                    .build();
            cronScheduler.scheduleJob(job, cronTrigger);
            cronScheduler.start();
            try {
                /* 为观察程序运行，此设置主程序睡眠2分钟才继续往下运行（因下一个步骤是“关闭Scheduler”） */
                TimeUnit.MINUTES.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 关闭Scheduler
            cronScheduler.shutdown();
        } catch (SchedulerException se) {
            logger.error(se.getMessage(), se);
        }
    }
}