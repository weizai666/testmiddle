package com.hyts.assemble.authsecurity.rbac.srv.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyts.assemble.authsecurity.rbac.dao.SysUserRoleDao;
import com.hyts.assemble.authsecurity.rbac.po.SystemUserRole;
import com.hyts.assemble.authsecurity.rbac.srv.SysUserRoleService;
import org.springframework.stereotype.Service;

/**
 * @Description 用户与角色业务实现
 * @Author Sans
 * @CreateTime 2019/9/14 15:57
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SystemUserRole> implements SysUserRoleService {

}