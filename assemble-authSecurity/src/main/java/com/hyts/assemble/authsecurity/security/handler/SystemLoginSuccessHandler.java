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

import com.alibaba.fastjson.JSONObject;
import com.hyts.assemble.authsecurity.consts.AuthKeyProperties;
import com.hyts.assemble.authsecurity.consts.JwtKeyProperties;
import com.hyts.assemble.authsecurity.domain.SystemUserSubject;
import com.hyts.assemble.authsecurity.jwt.JwtConfigProperties;
import com.hyts.assemble.authsecurity.jwt.JwtTokenService;
import com.hyts.assemble.authsecurity.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.security.handler
 * @author:LiBo/Alex
 * @create-date:2021-12-25 9:55 下午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
@Component
public class SystemLoginSuccessHandler implements AuthenticationSuccessHandler {


    
    @Autowired
    private JwtConfigProperties jwtConfigProperties;


    @Autowired
    private JwtTokenService jwtTokenService;


    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 执行返回先关的数据模型机制
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SystemUserSubject selfUserEntity =  (SystemUserSubject) authentication.getPrincipal();
        String token = jwtTokenService.createAccessToken(selfUserEntity);
        log.info("「获取到了相关的登录成功后的token信息:{}」",token);
        redisTemplate.opsForValue().set(token, JSONObject.toJSONString(selfUserEntity),10, TimeUnit.MINUTES);
        token = jwtConfigProperties.getTokenPrefix()+token;
        log.info("「拼接后相关的登录成功后的token信息:{}」",token);
        Map<String,Object> resultData = new HashMap<>();
        resultData.put(AuthKeyProperties.RESULT_VALUE_KEY.getCode()
                ,AuthKeyProperties.LOGIN_SUCCESS.getCode());
        resultData.put(AuthKeyProperties.RESULT_VALUE_KEY.getValue()
                ,AuthKeyProperties.LOGIN_SUCCESS.getValue());
        resultData.put(JwtKeyProperties.DEFAULT_TOKEN_KEY,token);
        ResultUtil.responseJson(response,resultData);

    }
}
