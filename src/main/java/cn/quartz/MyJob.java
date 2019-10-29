package cn.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyJob {
    Logger log = LoggerFactory.getLogger(MyJob.class);
    public void work () {
        // 此任务仅打印日志便于调试、观察
        log.info("==================:"+this.getClass().getName() + " was just triggered...");
    }
}
