/**
 * Copyright [2019] [LiBo/Alex of copyright liboware@gmail.com ]
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
package com.hyts.assemble.authsecurity.authen;

import com.hyts.assemble.authsecurity.rbac.po.SystemMenu;
import com.hyts.assemble.authsecurity.rbac.po.SystemRole;
import com.hyts.assemble.authsecurity.rbac.po.SystemUser;
import com.hyts.assemble.authsecurity.rbac.srv.SysUserService;
import com.hyts.assemble.common.model.rpc.RpcRequest;
import com.hyts.assemble.common.model.rpc.RpcResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.account.rpc
 * @author:LiBo/Alex
 * @create-date:2022-01-22 18:03
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */

@Slf4j
@Service
public class RpcAccountProcessApi implements AccountProcessApi {



    @Autowired
    SysUserService sysUserService;

    /**
     * 根据用户名称查询用户信息
     * @param username
     * @return
     */
    @Override
    public RpcResponse<SystemUser> selectUserByName(RpcRequest<String> username) {
        try {
            return RpcResponse.success(sysUserService.selectUserByName(username.getEnity()));
        } catch (Exception e) {
            log.error("执行查询用户信息根据名称失败！",e);
            return RpcResponse.failure("执行查询用户信息根据名称失败");
        }
    }

    /**
     * 执行查询用户角色信息
     * @param userId
     * @return
     */
    @Override
    public RpcResponse<List<SystemRole>> selectSysRoleByUserId(RpcRequest<Long> userId) {
        try {
            return RpcResponse.success(sysUserService.selectSysRoleByUserId(userId.getEnity()));
        } catch (Exception e) {
            log.error("执行查询用户角色信息失败！",e);
            return RpcResponse.failure("执行查询用户角色信息失败");
        }
    }

    /**
     * 执行查询用户资源菜单信息
     * @param userId
     * @return
     */
    @Override
    public RpcResponse<List<SystemMenu>> selectSysMenuByUserId(RpcRequest<Long> userId) {
        try {
            return RpcResponse.success(sysUserService.selectSysMenuByUserId(userId.getEnity()));
        } catch (Exception e) {
            log.error("执行查询用户资源菜单信息失败！",e);
            return RpcResponse.failure("执行查询用户资源菜单信息失败");
        }
    }
}
