package com.hyts.assemble.authsecurity.security.handler;

import com.hyts.assemble.authsecurity.consts.AuthKeyProperties;
import com.hyts.assemble.authsecurity.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 暂无权限处理类
 * @Author Sans
 * @CreateTime 2019/10/3 8:39
 */
@Slf4j
@Component
public class SystemAuthAccessDeniedHandler implements AccessDeniedHandler{

    /**
     * 暂无权限返回结果
     * @Author Sans
     * @CreateTime 2019/10/3 8:41
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception){
        ResultUtil.responseJson(response,ResultUtil.resultCode(Integer.parseInt(AuthKeyProperties.DENIED_ACCESS.getCode()),
                AuthKeyProperties.DENIED_ACCESS.getValue()));
    }
}