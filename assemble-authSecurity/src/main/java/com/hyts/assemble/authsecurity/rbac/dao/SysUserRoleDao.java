package com.hyts.assemble.authsecurity.rbac.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyts.assemble.authsecurity.rbac.po.SystemUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 用户与角色关系DAO
 * @Author Sans
 * @CreateTime 2019/9/14 15:57
 */
public interface SysUserRoleDao extends BaseMapper<SystemUserRole> {
	
}
