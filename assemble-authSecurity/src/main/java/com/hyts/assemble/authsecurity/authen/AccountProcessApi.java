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
import com.hyts.assemble.common.model.rpc.RpcRequest;
import com.hyts.assemble.common.model.rpc.RpcResponse;
import java.util.List;

/**
 * @project-name:callcenter
 * @package-name:com.hyts.callcenter.account
 * @author:LiBo/Alex
 * @create-date:2022-01-22 17:50
 * @copyright:libo-alex4java
 * @email:liboware@gmail.com
 * @description:
 */
public interface AccountProcessApi {

    /**
     * 根据用户名查询实体
     * @Author Sans
     * @CreateTime 2019/9/14 16:30
     * @Param  username 用户名
     * @Return SysUserEntity 用户实体
     */
    RpcResponse<SystemUser> selectUserByName(RpcRequest<String> username);

    /**
     * 根据用户ID查询角色集合
     * @Author Sans
     * @CreateTime 2019/9/18 18:01
     * @Param  userId 用户ID
     * @Return List<SysRoleEntity> 角色名集合
     */
    RpcResponse<List<SystemRole>> selectSysRoleByUserId(RpcRequest<Long> userId);

    /**
     * 根据用户ID查询权限集合
     * @Author Sans
     * @CreateTime 2019/9/18 18:01
     * @Param  userId 用户ID
     * @Return List<SysMenuEntity> 角色名集合
     */
    RpcResponse<List<SystemMenu>> selectSysMenuByUserId(RpcRequest<Long> userId);


}
