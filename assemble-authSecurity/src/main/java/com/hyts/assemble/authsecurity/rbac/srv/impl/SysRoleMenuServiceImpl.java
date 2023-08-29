package com.hyts.assemble.authsecurity.rbac.srv.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyts.assemble.authsecurity.rbac.dao.SysRoleMenuDao;
import com.hyts.assemble.authsecurity.rbac.po.SystemRoleMenu;
import com.hyts.assemble.authsecurity.rbac.srv.SysRoleMenuService;
import org.springframework.stereotype.Service;

/**
 * @Description 角色与权限业务实现
 * @Author Sans
 * @CreateTime 2019/9/14 15:57
 */
@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuDao, SystemRoleMenu> implements SysRoleMenuService {

}