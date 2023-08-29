/**
 * Copyright [2019] [LiBo/Alex of copyright liboware@gmail.com ]
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
package com.hyts.assemble.redisdelayer.web;

import com.hyts.assemble.common.model.http.ResultResponse;
import com.hyts.assemble.redisdelayer.model.ExecuteInvokerEvent;
import com.hyts.assemble.redisdelayer.redis.DelayedRedissionClientTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @project-name:assemble
 * @package-name:com.hyts.assemble.redisdelayer.web
 * @author:LiBo/Alex
 * @create-date:2022-05-24 19:19
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
@RequestMapping("/api/delayed")
@RestController
@Api(value="延时队列服务",tags = {"延时队列服务组件"},description = "延时队列服务组件")
public class DelayerQueueController {


    @Autowired
    DelayedRedissionClientTool delayedRedissionClientTool;


    /**
     * 创建对应的延时队列对象服务
     * @param executeInvokerEvent
     * @return
     */
    @PostMapping("/publish")
    @ApiOperation(value="添加延时队列任务",notes = "添加延时队列任务")
    public ResultResponse publish(@RequestBody ExecuteInvokerEvent executeInvokerEvent){
        try {
            delayedRedissionClientTool.offer(executeInvokerEvent);
            return ResultResponse.success(executeInvokerEvent);
        }catch (Exception e){
            log.error("create delaye element is failure!",e);
            return ResultResponse.failure("create delaye element is failure!");
        }
    }

}
