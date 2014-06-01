package net.nifoo.security.service;

import net.nifoo.security.entity.Permission;

public interface PermissionService {

	public Permission createPermission(Permission permission);

	public void deletePermission(Long permissionId);

}