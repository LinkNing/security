package net.nifoo.security.service;

import org.springframework.transaction.annotation.Transactional;

import net.nifoo.security.entity.Permission;

@Transactional
public interface PermissionService {

	public Permission createPermission(Permission permission);

	public void deletePermission(Long permissionId);

}