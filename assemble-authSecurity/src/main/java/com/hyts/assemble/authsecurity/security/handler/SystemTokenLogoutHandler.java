package com.hyts.assemble.authsecurity.security.handler;

import com.hyts.assemble.authsecurity.jwt.JwtConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 登出业务逻辑类
 * </p>
 *
 * @author qy
 * @since 2019-11-08
 */
@Component
public class SystemTokenLogoutHandler implements LogoutHandler {


    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private JwtConfigProperties jwtConfigProperties;


    public SystemTokenLogoutHandler(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 执行相关的登出操作
     * @param request
     * @param response
     * @param authentication
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String tokenHeader = request.getHeader(jwtConfigProperties.getTokenHeader());
        if (tokenHeader != null) {
            String token = tokenHeader.replace(jwtConfigProperties.getTokenPrefix(), "");
            //清空当前用户缓存中的权限数据
            redisTemplate.delete(token);
        }
    }

}