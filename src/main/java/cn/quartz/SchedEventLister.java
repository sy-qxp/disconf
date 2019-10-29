package cn.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedEventLister implements Runnable{
    private static Logger log = LoggerFactory.getLogger(SchedEventLister.class);
    private Scheduler sched = null;
    private TriggerKey triggerKey = null;

    public SchedEventLister(TriggerKey triggerKey,Scheduler sched) {
        log.info("触发器线程监听启动....");
        this.triggerKey = triggerKey;
        this.sched = sched;
    }

    public void run() {

        log.info("触发器线程监听中。。。。"
                + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                .format(new Date()));

        // 获取当前触发器的状态
        Trigger.TriggerState ts = null;
        try {
            ts = sched.getTriggerState(triggerKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        // 如果为暂停状态就恢复
        if (ts == Trigger.TriggerState.PAUSED) {

            // 暂停10秒在恢复
            try {
                log.warn("暂停当前线程5秒钟后在恢复触发器...,触发器组名: " + triggerKey.getGroup()
                        + ",触发器名称: " + triggerKey.getName());
                Thread.sleep(5L * 1000L);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            // 恢复触发器
            log.warn("正在恢复了触发器...,触发器组名: " + triggerKey.getGroup()
                    + ",触发器名称: " + triggerKey.getName());
            try {
                sched.resumeTrigger(triggerKey);
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }

        } else if (ts == Trigger.TriggerState.NORMAL) {
            log.info("触发器线程监听中。。。。,状态：正常 ");
        } else if (ts == Trigger.TriggerState.NONE) {
            log.info("触发器线程监听中。。。。,状态：没有触发器 ");
        } else if (ts == Trigger.TriggerState.ERROR) {
            log.info("触发器线程监听中。。。。,状态：错误 ");
        } else if (ts == Trigger.TriggerState.BLOCKED) {
            log.info("触发器线程监听中。。。。,状态：堵塞 ");
        }
    }
}

