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
package com.hyts.assemble.taskqueue.web;

import com.hyts.assemble.common.model.http.ResultRequest;
import com.hyts.assemble.common.model.http.ResultResponse;
import com.hyts.assemble.taskqueue.aop.ExecuteTargetProxy;
import com.hyts.assemble.taskqueue.serv.DefaultTaskQueueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.taskqueue.web
 * @author:LiBo/Alex
 * @create-date:2022-05-26 00:27
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
@RequestMapping("/api/taskqueue")
@RestController
@Api(value="任务队列服务",tags = {"任务队列服务组件"},description = "通过redisTemplate进行实现")
public class TaskQueueController {

    @Autowired
    DefaultTaskQueueService defaultTaskQueueService;

    /**
     * 创建key
     * @param queueId
     * @return
     */
    @GetMapping("/create")
    @ApiOperation(value="创建任务",notes = "添加任务对象模型到任务队列中")
    public ResultResponse createTaskQueue(@RequestParam("queueId") String queueId,
                                          @RequestParam("entity") String entity){
        try {
            ExecuteTargetProxy.getExecuteTaskCurrentContext().setProxyEnv("default");
            defaultTaskQueueService.taskQueue(new ResultRequest(queueId,entity));
            return ResultResponse.success();
        }catch (Exception e){
            log.error("keyGenerator is failure!",e);
            return ResultResponse.failure("keyGenerator is failure!");
        }
    }
}
