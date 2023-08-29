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
package com.hyts.assemble.taskqueue.logic;

import com.hyts.assemble.taskqueue.constant.DefaultExecuteTaskType;
import com.hyts.assemble.taskqueue.constant.ExecuteTaskType;
import com.hyts.assemble.taskqueue.logic.AbstractExecuteTaskService;
import com.hyts.assemble.taskqueue.model.ExecuteProcessResult;
import com.hyts.assemble.taskqueue.model.ExecuteTaskDataDTO;
import com.hyts.assemble.taskqueue.model.ExecuteTaskInfo;
import com.hyts.assemble.taskqueue.model.ExecuteTaskVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;


/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.engine.queue
 * @author:LiBo/Alex
 * @create-date:2021-12-11 11:29 上午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
@Service("DefaultExecuteTaskType")
public class DefaultExecuteTaskService extends AbstractExecuteTaskService {



    @PostConstruct
    void init(){
        initExecuteWorkTask();
    }



    @Override
    protected ExecuteProcessResult preExecuteTask(ExecuteTaskDataDTO executeTaskDataDTO) {
        log.info("preExecuteTask 前置执行操作");
        return ExecuteProcessResult.success(0L,"preExecuteTask 前置执行操作成功");
    }

    /**
     * 后置处理控制机制
     * @param executeTaskDataDTO
     * @param proxyMethodResult
     * @return
     */
    @Override
    protected ExecuteProcessResult postExecuteTask(ExecuteTaskDataDTO executeTaskDataDTO, Object proxyMethodResult) {
        log.info("postExecuteTask 后置执行操作");
        return ExecuteProcessResult.success(0L,"postExecuteTask 后置执行操作成功");
    }



    @Override
    protected ExecuteTaskType currentExecuteType() {
        return DefaultExecuteTaskType.DEFAULT_INSTANCE;
    }



    @SuppressWarnings("all")
    @Override
    protected <T extends ExecuteTaskVO> T viewModelResolver(ExecuteTaskInfo executeTaskInfo) {
        return (T) new ExecuteTaskVO(executeTaskInfo);
    }



    @Override
    protected String getTaskExecuteQueue() {
        return "EXECUTE:TASK:QUEUE:";
    }
}
