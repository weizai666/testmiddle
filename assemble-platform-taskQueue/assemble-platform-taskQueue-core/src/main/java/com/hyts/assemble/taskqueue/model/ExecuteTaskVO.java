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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hyts.assemble.taskqueue.constant.ExecuteResultType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @project-name:wiz-sound-ai2
 * @package-name:com.wiz.soundai.task.domain.vo.execute
 * @author:LiBo/Alex
 * @create-date:2021-09-27 11:37
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@ToString
@NoArgsConstructor
@Data
@SuppressWarnings("serial")
public class ExecuteTaskVO implements Serializable {


    private Long taskId;


    private String taskCode;


    private String taskName;


    private Byte taskStatus;


    @JsonFormat(pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT+8")
    private Date createTime;


    private String taskStatusName;


    /**
     * 数据模型转换操作机制控制
     */
    public ExecuteTaskVO(ExecuteTaskInfo executeTaskInfo){
        // 执行任务VO数据视图对象
        this.setTaskId(executeTaskInfo.getExecuteTaskId());
        this.setTaskCode(executeTaskInfo.getExecuteTaskCode());
        this.setCreateTime(executeTaskInfo.getCreateTime());
        this.setTaskName(executeTaskInfo.getExecuteTaskName());
        this.setTaskStatus(executeTaskInfo.getExecuteTaskStatus());
        this.setTaskStatusName(ExecuteResultType.getMeByTypeCode(this.taskStatus).getMessage());
        extendpOToVO(executeTaskInfo);
    }


    protected void extendpOToVO(ExecuteTaskInfo executeTaskInfo){
    }

}
