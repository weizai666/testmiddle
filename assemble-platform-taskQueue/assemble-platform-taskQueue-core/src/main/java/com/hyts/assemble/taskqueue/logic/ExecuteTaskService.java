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
package com.hyts.assemble.taskqueue.logic;

import cn.hutool.db.PageResult;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.hyts.assemble.taskqueue.model.ExecuteTaskDataDTO;
import com.hyts.assemble.taskqueue.model.ExecuteTaskRequestDTO;
import com.hyts.assemble.taskqueue.model.ExecuteTaskVO;

/**
 * @project-name:wiz-sound-ai2
 * @package-name:com.wiz.soundai.task.service
 * @author:LiBo/Alex
 * @create-date:2021-09-27 11:27
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public interface ExecuteTaskService {

    /**
     * 任务执行编码的前缀标识符
     */
    String DEFAULT_TASK_EXECUTE_CODE_PREFIX = "exec_task:";

    String DEFAULT_TASK_EXECUTE_CODE_DATA_PREFIX = "EX";

    /**
     * 执行任务操作线程机制
     */
    void initExecuteWorkTask();

    /**
     * 新增执行任务
     */
    void addExecuteTask(ExecuteTaskDataDTO executeTaskDataDTO);

    /**
     * 核心方法，执行调用任务
     * @param executeTaskDataDTO
     */
    void callExecuteTask(ExecuteTaskDataDTO executeTaskDataDTO);

    /**
     * 删除执行任务
     */
    void deleteExecuteTask(ExecuteTaskRequestDTO executeTaskRequestDTO);

    /**
     * 停止执行任务
     */
    void stopExecuteTask(ExecuteTaskRequestDTO executeTaskRequestDTO);

    /**
     * 更新执行数据状态(内部使用)
     */
    void updateExecuteTask(ExecuteTaskDataDTO executeTaskRequestDTO);

    /**
     * 核心方法，重试调用任务
     * @param executeTaskDataDTO
     */
    void retryExecuteTask(ExecuteTaskDataDTO executeTaskDataDTO);

    /**
     * 提供给子类自定义重写核心数据模型
     * @param executeTaskDataDTO
     * @return
     */
    Object invokeExecuteMethod(ExecuteTaskDataDTO executeTaskDataDTO);


   /***
    * 查询对应的执行任务
    * @param executeTaskRequestDTO
    * @return
    */
    PageResult<? extends ExecuteTaskVO> queryExecuteTask(ExecuteTaskRequestDTO executeTaskRequestDTO);


    Splitter SPLITTER = Splitter.on(",").trimResults();


    Joiner JOINER = Joiner.on("_").skipNulls();

}
