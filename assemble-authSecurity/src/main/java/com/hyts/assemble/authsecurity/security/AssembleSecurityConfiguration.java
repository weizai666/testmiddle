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
package com.hyts.assemble.authsecurity.security;

import com.hyts.assemble.authsecurity.access.AccessListController;
import com.hyts.assemble.authsecurity.config.SecurityDynamicProperties;
import com.hyts.assemble.authsecurity.jwt.JWTAuthenticationTokenFilter;
import com.hyts.assemble.authsecurity.jwt.JwtConfigProperties;
import com.hyts.assemble.authsecurity.security.handler.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

//SpringSecurity配置类

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.security
 * @author:LiBo/Alex
 * @create-date:2021-12-25 8:39 下午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启权限注解,默认是关闭的
@Configuration
public class AssembleSecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    SystemAuthenticationProvider systemAuthenticationProvider;

    /**
     * 自定义登录成功处理器
     */
    @Autowired
    private SystemLoginSuccessHandler userLoginSuccessHandler;

    /**
     * 自定义登录失败处理器
     */
    @Autowired
    private SystemLoginFailureHandler userLoginFailureHandler;

    /**
     * 自定义注销成功处理器
     */
    @Autowired
    private SystemLogoutSuccessHandler userLogoutSuccessHandler;

    /**
     * 自定义暂无权限处理器
     */
    @Autowired
    private SystemAuthAccessDeniedHandler userAuthAccessDeniedHandler;

    /**
     * 系统认证未授权操作处理节点机制
     */
    @Autowired
    private SystemUnauthorizedEntryPoint systemUnauthorizedEntryPoint;

    /**
     * 自定义未登录的处理器
     */
    @Autowired
    private SystemAuthenticationEntryPointHandler userAuthenticationEntryPointHandler;

    /**
     * jwt的配置属性信息对象
     */
    @Autowired
    private JwtConfigProperties jwtConfigProperties;

    /**
     * system系统token登出操作处理器
     */
    @Autowired
    private SystemTokenLogoutHandler systemTokenLogoutHandler;


    /**
     * system系统的控制列表机制
     */
    @Autowired
    private AccessListController accessListController;


    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    SecurityDynamicProperties securityDynamicProperties;

    /**
     * 加密方式
     * @Author Sans
     * @CreateTime 2019/10/1 14:00
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 注入自定义PermissionEvaluator
     */
    @Bean
    public DefaultWebSecurityExpressionHandler userSecurityExpressionHandler(SystemPermissionEvaluator systemPermissionEvaluator){
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(systemPermissionEvaluator);
        return handler;
    }

    /**
     * 配置登录验证逻辑
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        //这里可启用我们自己的登陆验证逻辑
        auth.authenticationProvider(systemAuthenticationProvider);
    }

    /**
     * 配置security的控制逻辑
     * @Author Sans
     * @CreateTime 2019/10/1 16:56
     * @Param  http 请求
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("执行查询动态参数机制:{}",securityDynamicProperties);
        http.authorizeRequests()
                // 不进行权限验证的请求或资源(从配置文件中读取)
                .antMatchers(jwtConfigProperties.getAntMatchers()
                        .split(",")).permitAll()
                // 其他的需要登陆后才能访问
                .anyRequest().authenticated()
                .and()
                // 配置未登录自定义处理类
                .httpBasic().authenticationEntryPoint(userAuthenticationEntryPointHandler)
                .and()
                // 配置登录地址
                .formLogin().usernameParameter("userName").passwordParameter("password")
                .loginProcessingUrl("/account/login")
                // 配置登录成功自定义处理类
                .successHandler(userLoginSuccessHandler)
                // 配置登录失败自定义处理类
                .failureHandler(userLoginFailureHandler)
                .and()
                // 配置登出地址
                .logout()
                .logoutUrl("/account/logout")
                // 配置用户登出自定义处理类
                .addLogoutHandler(systemTokenLogoutHandler)
                .logoutSuccessHandler(userLogoutSuccessHandler)
                .and()
                // 配置没有权限自定义处理类
                .exceptionHandling().accessDeniedHandler(userAuthAccessDeniedHandler)
                .authenticationEntryPoint(systemUnauthorizedEntryPoint)
                .and()
                // 开启跨域
                .cors()
                .and()
                // 取消跨站请求伪造防护
                .csrf().disable();

        // 基于Token不需要session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 禁用缓存
        http.headers().cacheControl();
        // 添加JWT过滤器
        http.addFilter(new JWTAuthenticationTokenFilter(authenticationManager(),jwtConfigProperties,accessListController,redisTemplate));
    }

}
