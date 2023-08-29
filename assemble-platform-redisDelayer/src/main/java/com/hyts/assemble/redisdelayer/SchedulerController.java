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
package com.hyts.assemble.redisdelayer;

import org.redisson.RedissonExecutorService;
import org.redisson.api.CronSchedule;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RScheduledFuture;
import org.redisson.api.RedissonClient;
import org.redisson.executor.RedissonExecutorRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.process.tasker
 * @author:LiBo/Alex
 * @create-date:2022-05-31 22:34
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Component
public class SchedulerController {



    RedissonExecutorRemoteService executorRemoteService;


    RedissonExecutorService redissonExecutorService;


    @Autowired
    RedissonClient redissonClient;



    public void publicSchedulerTask(){
        RScheduledExecutorService scheduledExecutorService=
                redissonClient.getExecutorService("taskScheduler");
//        scheduledExecutorService.scheduleAtFixedRate(new Task(),1,3, TimeUnit.SECONDS);
        RScheduledFuture rScheduledFuture = scheduledExecutorService.scheduleAsync(new Task(), CronSchedule.of("* * * * * ?"));
        try {
            System.out.println(rScheduledFuture.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public static class Task implements Runnable, Serializable {
        @Override
        public void run() {
            System.out.println("execute task :{}"+System.currentTimeMillis());
        }
    };
}
