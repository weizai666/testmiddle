package com.hyts.assemble.authsecurity.rbac.srv.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyts.assemble.authsecurity.rbac.dao.SysRoleDao;
import com.hyts.assemble.authsecurity.rbac.po.SystemRole;
import com.hyts.assemble.authsecurity.rbac.srv.SysRoleService;
import org.springframework.stereotype.Service;

/**
 * @Description 角色业务实现
 * @Author Sans
 * @CreateTime 2019/9/14 15:57
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SystemRole> implements SysRoleService {

}