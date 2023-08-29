package com.hyts.assemble.authsecurity.security.handler;

import com.google.common.collect.Lists;
import com.hyts.assemble.authsecurity.authen.AccountProcessApi;
import com.hyts.assemble.authsecurity.domain.SystemUserSubject;
import com.hyts.assemble.authsecurity.rbac.po.SystemRole;
import com.hyts.assemble.authsecurity.security.service.SystemUserDataService;
import com.hyts.assemble.common.model.rpc.RpcRequest;
import com.hyts.assemble.common.model.rpc.RpcResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 自定义登录验证
 * @Author Sans
 * @CreateTime 2019/10/1 19:11
 */
@Component
@Slf4j
public class SystemAuthenticationProvider implements AuthenticationProvider {


    @Autowired
    private SystemUserDataService systemUserDataService;


    @Autowired
    private AccountProcessApi accountProcessApi;


    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取表单输入中返回的用户名
        String userName = (String) authentication.getPrincipal();
        // 获取表单中输入的密码
        String password = (String) authentication.getCredentials();
        log.info("userName:{} password:{}",userName,password);
        // 查询用户是否存在
        if("ANON".equalsIgnoreCase(userName)){
            return new UsernamePasswordAuthenticationToken(null, password, Lists.newArrayList());
        }
        SystemUserSubject userInfo = systemUserDataService.loadUserByUsername(userName);
        if (Objects.isNull(userInfo)) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        // 我们还要判断密码是否正确，这里我们的密码使用BCryptPasswordEncoder进行加密的
        if (!bCryptPasswordEncoder.matches(password, userInfo.getPassword())) {
            throw new BadCredentialsException("密码不正确");
        }
        // 还可以加一些其他信息的判断，比如用户账号已停用等判断
        if (userInfo.getStatus().equals("PROHIBIT")){
            throw new LockedException("该用户已被冻结");
        }
        // 角色集合
        Set<GrantedAuthority> authorities = new HashSet<>();
        // 查询用户角色
        RpcResponse<List<SystemRole>> result = accountProcessApi.selectSysRoleByUserId(new RpcRequest<>(userInfo.getUserId()));
        if(result.isSuccess()) {
            List<SystemRole> sysRoleEntityList = result.getEntity();
            for (SystemRole sysRoleEntity : sysRoleEntityList) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + sysRoleEntity.getRoleName()));
            }
            userInfo.setAuthorities(authorities);
            // 进行登录
            return new UsernamePasswordAuthenticationToken(userInfo, password, authorities);
        }else{
            throw new NonceExpiredException("rpc 调用服务失败！");
        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}