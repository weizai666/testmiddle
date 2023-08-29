/**
 * Copyright [2020] [LiBo/Alex of copyright liboware@gmail.com ]
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyts.assemble.scheduler.core;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.scheduler
 * @author:LiBo/Alex
 * @create-date:2022-06-27 14:23
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
public class QuartzSchedulerHandler {


    @Autowired
    Map<String,QuartzJob> quartzJobMap = Maps.newHashMap();


    /**
     * @author liulianjia
     * @Description 任务调度
     * @date 2022/5/30 17:14
     * @param
     **/
    @Autowired
    private Scheduler scheduler;


    /**
     * 开始执行所有任务
     *
     * @throws SchedulerException
     */
    public void launcherJobs() throws SchedulerException {
        // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
        // JobDetail 是具体Job实例
        if(CollectionUtil.isNotEmpty(quartzJobMap)){
            quartzJobMap.entrySet().forEach(param->{
                JobDetail jobDetail = JobBuilder.newJob(param.getValue().getClass()).
                        withIdentity(param.getKey()+SchedulerQuartzConstant.JOB_NAME,
                        param.getKey().trim()+SchedulerQuartzConstant.JOB_GROUP_NAME).build();
                // 基于表达式构建触发器
                CronScheduleBuilder cronScheduleBuilder =
                        CronScheduleBuilder.cronSchedule(param.getValue().cronExpression());
                // CronTrigger表达式触发器 继承于Trigger
                // TriggerBuilder 用于构建触发器实例
                CronTrigger cronTrigger = TriggerBuilder.newTrigger().
                                withIdentity(param.getKey()+SchedulerQuartzConstant.TRIGGER_NAME,
                                param.getKey()+SchedulerQuartzConstant.TRIGGER_GROUP_NAME)
                        .withSchedule(cronScheduleBuilder).build();
                try {
                    scheduler.scheduleJob(jobDetail, cronTrigger);
                } catch (SchedulerException e) {
                    log.error("scheduleJob is failure!",e);
                }
            });
        }
        scheduler.start();
    }

    /**
     * start的操作job
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void startJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return;
        }
    }

    /**
     * 获取Job信息
     *
     * @param name
     * @param group
     * @return
     * @throws SchedulerException
     */
    public String getJobInfo(String name, String group) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        return String.format("time:%s,state:%s", cronTrigger.getCronExpression(),
                scheduler.getTriggerState(triggerKey).name());
    }

    /**
     * 修改某个任务的执行时间
     *
     * @param name
     * @param group
     * @param time
     * @return
     * @throws SchedulerException
     */
    public boolean modifyJob(String name, String group, String time) throws SchedulerException {
        Date date = null;
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldTime = cronTrigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(time)) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
                    .withSchedule(cronScheduleBuilder).build();
            date = scheduler.rescheduleJob(triggerKey, trigger);
        }
        return date != null;
    }

    /**
     * 暂停所有任务
     *
     * @throws SchedulerException
     */
    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }

    /**
     * 暂停某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void pauseJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return;
        }
        scheduler.pauseJob(jobKey);
    }


    /**
     * 恢复所有任务
     *
     * @throws SchedulerException
     */
    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    /**
     * 恢复某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void resumeJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return;
        }
        scheduler.resumeJob(jobKey);
    }


    /**
     * 删除某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void deleteJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return;
        }
        scheduler.deleteJob(jobKey);
    }

}
