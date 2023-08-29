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
package com.hyts.assemble.authsecurity.security.handler;

import com.hyts.assemble.authsecurity.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.security.handler
 * @author:LiBo/Alex
 * @create-date:2021-12-25 10:46 下午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: 登录失败返回结果
 */
@Slf4j
@Component
public class SystemLoginFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 这些对于操作的处理类可以根据不同异常进行不同处理
        if (exception instanceof UsernameNotFoundException){
            log.error("【登录失败】",exception);
            ResultUtil.responseJson(response,ResultUtil.resultCode(500,"用户名不存在"));
        }
        else if (exception instanceof LockedException){
            log.error("【登录失败】",exception);
            ResultUtil.responseJson(response,ResultUtil.resultCode(500,"用户被冻结"));
        }
        else if (exception instanceof BadCredentialsException){
            log.error("【登录失败】",exception);
            ResultUtil.responseJson(response,ResultUtil.resultCode(500,"用户名密码不正确"));
        }else{
            ResultUtil.responseJson(response,ResultUtil.resultCode(500,"登录失败"));
        }
    }
}
