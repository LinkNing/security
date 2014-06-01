package net.nifoo.security.dao;

import net.nifoo.security.entity.Permission;

public interface PermissionDao {

	public Permission createPermission(Permission permission);

	public void deletePermission(Long permissionId);

}
