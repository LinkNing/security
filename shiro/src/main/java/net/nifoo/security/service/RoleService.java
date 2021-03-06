package net.nifoo.security.service;

import org.springframework.transaction.annotation.Transactional;

import net.nifoo.security.entity.Role;

@Transactional
public interface RoleService {
	public Role createRole(Role role);

	public void deleteRole(Long roleId);

	//添加角色-权限之间关系
	public void correlationPermissions(Long roleId, Long... permissionIds);

	//移除角色-权限之间关系
	public void uncorrelationPermissions(Long roleId, Long... permissionIds);//
}