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
package com.hyts.assemble.authsecurity.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.consts
 * @author:LiBo/Alex
 * @create-date:2021-12-25 10:37 下午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Getter
@AllArgsConstructor
public enum AuthKeyProperties {

    /**
     * 登录成功
     */
    LOGIN_SUCCESS("200","登录成功"),

    /**
     * 注销成功
     */
    LOGOUT_SUCCESS("200","注销成功"),

    /**
     * 执行成功
      */
    PROCESS_SUCCESS("200","执行成功"),

    /**
     * 未登录操作
     */
    NOT_LOGIN("401","未登录"),

    /**
     * 未登录操作
     */
    DENIED_ACCESS("401","拒接访问"),

    /**
     * 未授权操作
     */
    NOT_AUTH("403","未授权"),

    /**
     * token非法
     */
    TOKEN_FAILURE("402","token非法"),

    /**
     * 服务已经下线
     */
    SERVER_DOWN("405","服务已经下线！"),

    /**
     * token超时
     */
    TOKEN_TIME_OUT("406","token超时"),

    /**
     * 返回值的键值
     */
    RESULT_VALUE_KEY("code","msg"),

    ;

    private String code;

    private String value;

}
