package com.hyts.assemble.authsecurity.security.handler;

import com.hyts.assemble.authsecurity.consts.AuthKeyProperties;
import com.hyts.assemble.authsecurity.util.ResultUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户未登录处理类
 * @Author Sans
 * @CreateTime 2019/10/3 8:55
 */
@Component
public class SystemAuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    /**
     * 用户未登录返回结果
     * @Author Sans
     * @CreateTime 2019/10/3 9:01
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception){
        ResultUtil.responseJson(response,ResultUtil.resultCode(Integer.parseInt(AuthKeyProperties.NOT_LOGIN.getCode()),
                AuthKeyProperties.NOT_LOGIN.getValue()));
    }
}