package net.nifoo.security.service;

import javax.annotation.Resource;

import net.nifoo.security.dao.PermissionDao;
import net.nifoo.security.entity.Permission;

public class PermissionServiceImpl implements PermissionService {

	@Resource
	private PermissionDao permissionDao;

	public Permission createPermission(Permission permission) {
		return permissionDao.createPermission(permission);
	}

	public void deletePermission(Long permissionId) {
		permissionDao.deletePermission(permissionId);
	}
}
