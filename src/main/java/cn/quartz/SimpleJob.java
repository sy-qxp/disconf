package cn.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleJob implements Job {
    private static int jobCount = 0;
    Logger log = LoggerFactory.getLogger(SimpleJob.class);
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 此任务仅打印日志便于调试、观察
        log.info("==================:"+this.getClass().getName() + " was just triggered...");
        JobKey jobKey = context.getJobDetail().getKey();
        TriggerKey triggerKey = context.getTrigger().getKey();
        Scheduler scheduler =  context.getScheduler();
        //启动线程监听触发器的状态
        new Thread(new SchedEventLister(triggerKey,scheduler)).start();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        log.info("任务分组名称: " + jobKey.getGroup()+",任务名称: " +
                jobKey.getName()+",触发器组名称: " + triggerKey.getGroup()+ ",触发器名称: "+
                triggerKey.getName()+" 执行时间: " + sdf.format(new Date()) + ",计数: " + (++jobCount)) ;

        //如果任务执行超过15次就结束它
      /* if(jobCount > 3){
             log.warn("结束任务...");
             try {
                 //Delete the identified Job from the Scheduler - and any associated Triggers.
                 //从调度器中删除这个唯一任务时同时会删除相关联的触发器^_^
                 scheduler.deleteJob(jobKey);
             } catch (SchedulerException e) {
                 e.printStackTrace();
             }
         }*/
        if(jobCount == 2 ){
            //暂停触发器
            try {
                scheduler.pauseTrigger(triggerKey);
                log.warn("已经暂停了触发器...,触发器组名: " + triggerKey.getGroup()  + ",触发器名称: " + triggerKey.getName());
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }

        }
        if(jobCount == 4){
            try {
                scheduler.pauseTrigger(triggerKey); // 停止触发器
                scheduler.unscheduleJob(triggerKey);//移除触发器
                scheduler.pauseJob(jobKey);// 停止任务
                scheduler.deleteJob(jobKey);// 删除任务
                log.error("正在停止并且移除触发器...,触发器组名: " + triggerKey.getGroup()  + ",触发器名称: " + triggerKey.getName());
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }

    }

}