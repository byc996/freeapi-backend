package com.byc.job;

import com.byc.mapper.UserInterfaceInfoMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MonthlyTaskScheduler {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Scheduled(cron = "0 0 0 1 * ?")
    public void refreshQuota() {
        System.out.println("执行定时任务：刷新用户调用额度");
        userInterfaceInfoMapper.refreshRestNum();
    }
}
