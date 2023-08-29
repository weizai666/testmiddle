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

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @project-name:wiz-sound-ai2
 * @package-name:com.wiz.soundai.task.domain.dto.execute
 * @author:LiBo/Alex
 * @create-date:2021-09-27 11:36
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 执行任务查询数据传输对象
 */
@NoArgsConstructor
@Data
@SuppressWarnings("serial")
public class ExecuteTaskRequestDTO implements Serializable {


    private Long taskId;

    /**
     * 业务编码: 代指的是某一俄国
     */
    private String bizCode;

    /**
     * 业务类型
     */
    private Byte bizType;

    /**
     * 任务状态
     */
    private String taskStatus;

    /**
     * 任务结果额外信息数据
     */
    private String taskResultMessage;

    /**
     * 任务执行类型”TODO 暂时不划分任务执行类型
     */
    private Byte taskExecuteType;


    private Long userId;


    private Long departmentId;


    private Long enterpriseId;


    private int pageNo;


    private int pageSize;


    public ExecuteTaskRequestDTO(Long taskId) {
        this.taskId = taskId;
    }
}
