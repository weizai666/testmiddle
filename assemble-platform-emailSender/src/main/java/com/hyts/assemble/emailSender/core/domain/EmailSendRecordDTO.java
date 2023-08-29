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
package com.hyts.assemble.emailSender.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.emailSender.core.domain
 * @author:LiBo/Alex
 * @create-date:2022-08-13 18:49
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */

@NoArgsConstructor
@Accessors(chain = true)
@Data
public class EmailSendRecordDTO implements Serializable {


    private String namespace;

    /**
     * 标题
     */
    private String title;

    /**
     * 发送信息内容
     */
    private String content;

    /**
     * 0-实时发送,1-定时发送
     */
    private Integer type = 0;

    /**
     * 接收者邮箱，采用逗号分割。
     */
    private String receUsers;

    /**
     * 抄送人邮箱，采用逗号分割。
     */
    private String copyUsers;

    /**
     * 发送状态
     */
    private Integer sendStatus = 0;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 计划下一次发送时间
     */
    private LocalDateTime nextPlanDatetime = LocalDateTime.now();

    /**
     * 上一次发送时间
     */
    private LocalDateTime lastSendDatetime = LocalDateTime.now();

    /**
     * 0 未完成、1 已完成
     */
    private Integer executeStatus = 0;

    /**
     * 执行发送阈值
     */
    private Integer executeThreshold;

    /**
     * 执行发送次数
     */
    private Integer executeSendCount;

    /**
     * 0-是否html
     */
    private Integer isHtml = 0;


    private String attachments = "";

}
