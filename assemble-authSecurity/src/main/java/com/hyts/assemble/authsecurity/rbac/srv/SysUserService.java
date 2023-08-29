package com.hyts.assemble.authsecurity.rbac.srv;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hyts.assemble.authsecurity.rbac.po.SystemMenu;
import com.hyts.assemble.authsecurity.rbac.po.SystemRole;
import com.hyts.assemble.authsecurity.rbac.po.SystemUser;

import java.util.List;

/**
 * @Description 系统用户业务接口
 * @Author Sans
 * @CreateTime 2019/6/14 15:57
 */
public interface SysUserService extends IService<SystemUser>  {

    /**
     * 根据用户名查询实体
     * @Author Sans
     * @CreateTime 2019/9/14 16:30
     * @Param  username 用户名
     * @Return SysUserEntity 用户实体
     */
    SystemUser selectUserByName(String username);
    /**
     * 根据用户ID查询角色集合
     * @Author Sans
     * @CreateTime 2019/9/18 18:01
     * @Param  userId 用户ID
     * @Return List<SysRoleEntity> 角色名集合
     */
    List<SystemRole> selectSysRoleByUserId(Long userId);
    /**
     * 根据用户ID查询权限集合
     * @Author Sans
     * @CreateTime 2019/9/18 18:01
     * @Param  userId 用户ID
     * @Return List<SysMenuEntity> 角色名集合
     */
    List<SystemMenu> selectSysMenuByUserId(Long userId);

}