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
package com.hyts.assemble.taskqueue.model;

import com.hyts.assemble.taskqueue.constant.ExecuteTaskType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @project-name:wiz-sound-ai2
 * @package-name:com.wiz.soundai.task.domain.dto.execute
 * @author:LiBo/Alex
 * @create-date:2021-09-27 11:37
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExecuteTaskDataDTO {

    /**
     * 执行任务主键
     */
//    @NonNull
    private Long executeTaskId;

    /**
     * 执行业务编码
     */
//    @NonNull
    private String executeBizCode;

    /**
     * 执行业务类型
     */
//    @NonNull
    private Byte executeBizType;

    /**
     * 执行的任务类型
     */
//    @NonNull
    private Class<? extends ExecuteTaskType> executeTaskType;


    /**
     * 数据执行参数模型传输对象
     */
    private ExecuteMethodCallPoint executeMethodCallPoint;


    /**
     * 任务执行的错误信息
     */
    private String errorMessage;



    public ExecuteTaskDataDTO(Long executeTaskId, String executeBizCode, Byte executeBizType, Class<? extends ExecuteTaskType> executeTaskType, ExecuteMethodCallPoint executeMethodCallPoint) {
        this.executeTaskId = executeTaskId;
        this.executeBizCode = executeBizCode;
        this.executeBizType = executeBizType;
        this.executeTaskType = executeTaskType;
        this.executeMethodCallPoint = executeMethodCallPoint;
    }

    public ExecuteTaskDataDTO(Long executeTaskId, String executeBizCode, Byte executeBizType, Class<? extends ExecuteTaskType> executeTaskType, String errorMessage) {
        this.executeTaskId = executeTaskId;
        this.executeBizCode = executeBizCode;
        this.executeBizType = executeBizType;
        this.executeTaskType = executeTaskType;
        this.errorMessage = errorMessage;
    }

    /**
     * 执行任务的状态
     * ！！！灰常重要 当前传入的状态为非成功状态下的模型，则不会进入等待队列机制操作！
     */
    private Byte executeTaskStatus;

    /**
     * 执行任务的构造器
     * @param executeTaskId
     * @param executeBizCode
     * @param executeBizType
     * @param executeTaskType
     * @param errorMessage
     * @param executeTaskStatus
     */
    public ExecuteTaskDataDTO(Long executeTaskId, String executeBizCode, Byte executeBizType, Class<? extends ExecuteTaskType> executeTaskType, String errorMessage, Byte executeTaskStatus) {
        this.executeTaskId = executeTaskId;
        this.executeBizCode = executeBizCode;
        this.executeBizType = executeBizType;
        this.executeTaskType = executeTaskType;
        this.errorMessage = errorMessage;
        this.executeTaskStatus = executeTaskStatus;
    }
}
