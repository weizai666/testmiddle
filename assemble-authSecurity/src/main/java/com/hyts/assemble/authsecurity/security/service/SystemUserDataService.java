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
package com.hyts.assemble.authsecurity.security.service;

import com.hyts.assemble.authsecurity.authen.AccountProcessApi;
import com.hyts.assemble.authsecurity.domain.SystemUserSubject;
import com.hyts.assemble.authsecurity.rbac.po.SystemUser;
import com.hyts.assemble.common.model.rpc.RpcRequest;
import com.hyts.assemble.common.model.rpc.RpcResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.security.service
 * @author:LiBo/Alex
 * @create-date:2021-12-28 8:39 下午
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description: SpringSecurity用户的业务实现
 */

@Component
public class SystemUserDataService implements UserDetailsService {


    @Autowired
    AccountProcessApi accountProcessApi;


    @Override
    public SystemUserSubject loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        SystemUser sysUserEntity = null;
        RpcResponse<SystemUser> response = accountProcessApi.selectUserByName(new RpcRequest<String>().setEnity(username));
        if(response.isSuccess()){
            sysUserEntity = response.getEntity();
        }
        if (Objects.nonNull(sysUserEntity)){
            // 组装参数
            SystemUserSubject selfUserEntity = new SystemUserSubject();
            BeanUtils.copyProperties(sysUserEntity,selfUserEntity);
            return selfUserEntity;
        }
        return null;
    }
}
