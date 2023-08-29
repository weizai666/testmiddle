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

import com.hyts.assemble.taskqueue.constant.ExecuteResultType;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;

/**
 * @project-name:wiz-sound-ai2
 * @package-name:com.wiz.soundai.task.domain.dto.execute
 * @author:LiBo/Alex
 * @create-date:2021-09-28 11:07
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Data
public class ExecuteProcessResult {


    private boolean success;

    /**
     * 是否需要重试
     */
    private boolean needRetry;

    /**
     * 执行尺寸大小
     */
    private Long executeProcessResultSize;

    /**
     * 执行处理结果
     */
    private String executeProcessResult;

    /**
     * 执行失败的原因
     */
    private String executeProcessFailureMessage;


    /**
     * 成功操作处理机制
     * @return
     */
    public static ExecuteProcessResult success(Long executeProcessResultSize,String result){
        ExecuteProcessResult executeProcessResult = new ExecuteProcessResult();
        executeProcessResult.setNeedRetry(Boolean.FALSE);
        executeProcessResult.setSuccess(Boolean.TRUE);
        executeProcessResult.setExecuteProcessResult(StringUtils.defaultIfBlank(result, ExecuteResultType.FINISHED.getMessage()));
        executeProcessResult.setExecuteProcessResultSize(executeProcessResultSize);
        return executeProcessResult;
    }

    /**
     * 失败错误操作
     * @param failureMessage
     * @return
     */
    public static ExecuteProcessResult faliure(Long executeProcessResultSize,String failureMessage){
        return faliure(executeProcessResultSize,Strings.EMPTY,failureMessage,Boolean.TRUE);
    }

    /**
     * 失败操作处理机制
     * @param failureMessage
     * @return
     */
    public static ExecuteProcessResult faliure(Long executeProcessResultSize,String result,String failureMessage){
        return faliure(executeProcessResultSize,result,failureMessage,Boolean.TRUE);
    }

    /**
     * 失败操作处理机制
     * @param failureMessage
     * @param needRetry
     * @returnl
     */
    public static ExecuteProcessResult faliure(Long executeProcessResultSize,String result,String failureMessage,boolean needRetry){
        ExecuteProcessResult executeProcessResult = new ExecuteProcessResult();
        executeProcessResult.setExecuteProcessResult(result);
        executeProcessResult.setNeedRetry(needRetry);
        executeProcessResult.setExecuteProcessFailureMessage(failureMessage);
        executeProcessResult.setSuccess(Boolean.FALSE);
        executeProcessResult.setExecuteProcessResultSize(executeProcessResultSize);
        executeProcessResult.setExecuteProcessResultSize(0L);
        return executeProcessResult;
    }

}
