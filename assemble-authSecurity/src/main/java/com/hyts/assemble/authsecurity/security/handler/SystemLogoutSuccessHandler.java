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

import com.hyts.assemble.authsecurity.consts.AuthKeyProperties;
import com.hyts.assemble.authsecurity.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.security.handler
 * @author:LiBo/Alex
 * @create-date:2021-12-25 10:49 下午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
@Component
public class SystemLogoutSuccessHandler implements LogoutSuccessHandler {


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map<String,Object> resultData = new HashMap<>();
        resultData.put(AuthKeyProperties.RESULT_VALUE_KEY.getCode(),AuthKeyProperties.LOGOUT_SUCCESS.getCode());
        resultData.put(AuthKeyProperties.RESULT_VALUE_KEY.getValue(),AuthKeyProperties.LOGOUT_SUCCESS.getValue());
        SecurityContextHolder.clearContext();
        log.info("登出操作机制成功！");
        ResultUtil.responseJson(response, ResultUtil.resultSuccess(resultData));
    }
}
