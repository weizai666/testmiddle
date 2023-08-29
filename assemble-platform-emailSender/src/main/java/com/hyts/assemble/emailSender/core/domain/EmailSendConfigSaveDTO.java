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

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.annotation.TableField;
import com.hyts.assemble.emailSender.core.bean.EmailSendConfigEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @project-name:assemble-platform
 * @package-name:com.hyts.assemble.emailSender.core.domain
 * @author:LiBo/Alex
 * @create-date:2022-08-13 18:42
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 保存发送DTO数据模型
 */

@NoArgsConstructor
@Accessors(chain = true)
@Data
public class EmailSendConfigSaveDTO implements Serializable {



    private String namespace = UUID.fastUUID().toString();


    private String serverType;


    /**
     * 协议:smtpimap
     */
    private String protocol = "smtp";

    /**
     * 服务主机地址
     */
    private String hostname;

    /**
     * 端口
     */
    private Integer port = 25;

    /**
     * 用户
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 模板信息
     */
    private String template;

    /**
     * 消费发送utf8
     */
    private String encoding = "utf8";

    /**
     * 0-开启 1-关闭
     */
    private Integer enable = 0;


    /**
     * 构造器
     * @param hostname
     * @param username
     * @param password
     * @param template
     */
    public EmailSendConfigSaveDTO(String hostname, String username, String password, String template) {
        this.hostname = hostname;
        this.username = username;
        this.password = password;
        this.template = template;
    }


    public EmailSendConfigEntity dto2Po(){
        EmailSendConfigEntity emailSendConfigEntity = new EmailSendConfigEntity();
        emailSendConfigEntity.setEnable(enable);
        emailSendConfigEntity.setHostname(this.hostname);
        emailSendConfigEntity.setNamespace(this.namespace);
        emailSendConfigEntity.setPort(this.port);
        emailSendConfigEntity.setUsername(this.username);
        emailSendConfigEntity.setPassword(this.password);
        emailSendConfigEntity.setProtocol(this.protocol);
        emailSendConfigEntity.setTemplate(template);
        return emailSendConfigEntity;
    }


}
