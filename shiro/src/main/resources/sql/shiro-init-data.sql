DELETE FROM sys_users;
INSERT INTO sys_users(id, username, password, salt, locked) VALUES(1, 'admin', '562fd6ce396de63037503827e0bd4d2e', '61646d696e2d73616c74', FALSE);
INSERT INTO sys_users(id, username, password, salt, locked) VALUES(2, 'user', '067481fd7c3793d48e6846da9ff41404', 'dbeb9e9dd4850652692f531ab31aa97a', FALSE);

DELETE FROM sys_roles;
INSERT INTO sys_roles(id, role, description, available) VALUES(1, 'admin', 'admin', TRUE);
INSERT INTO sys_roles(id, role, description, available) VALUES(2, 'user', 'user', TRUE);

DELETE FROM sys_users_roles;
INSERT INTO sys_users_roles(user_id, role_id) VALUES(1, 1);
INSERT INTO sys_users_roles(user_id, role_id) VALUES(2, 2);

DELETE FROM sys_permissions;
INSERT INTO sys_permissions (id, permission, description, available) VALUES(1, 'admin:create,read,update,delete', 'admin s resource', TRUE);
INSERT INTO sys_permissions (id, permission, description, available) VALUES(2, 'user:create,read,update,delete', 'user s resource', TRUE);

DELETE FROM sys_roles_permissions;
INSERT INTO sys_roles_permissions(role_id, permission_id) VALUES(1, 1);
INSERT INTO sys_roles_permissions(role_id, permission_id) VALUES(2, 2);