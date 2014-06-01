package net.nifoo.security.service;

import javax.annotation.Resource;
import javax.sql.DataSource;

import net.nifoo.security.entity.Permission;
import net.nifoo.security.entity.Role;
import net.nifoo.security.entity.User;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("dev")
public abstract class BaseTest {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	protected PermissionService permissionService;
	@Autowired
	protected RoleService roleService;
	@Autowired
	protected UserService userService;

	protected String defaultPassword = "123";

	protected Permission p1;
	protected Permission p2;
	protected Permission p3;
	protected Role r1;
	protected Role r2;
	protected User u1;
	protected User u2;
	protected User u3;
	protected User u4;

	@Resource
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	//@Before
	public void setUp() {
		ThreadContext.unbindSubject(); //测试前解除绑定到线程的Subject

		jdbcTemplate.update("delete from sys_users");
		jdbcTemplate.update("delete from sys_roles");
		jdbcTemplate.update("delete from sys_permissions");
		jdbcTemplate.update("delete from sys_users_roles");
		jdbcTemplate.update("delete from sys_roles_permissions");

		//1、新增权限
		p1 = new Permission("user:create", "用户模块新增", Boolean.TRUE);
		p2 = new Permission("user:update", "用户模块修改", Boolean.TRUE);
		p3 = new Permission("menu:create", "菜单模块新增", Boolean.TRUE);
		permissionService.createPermission(p1);
		permissionService.createPermission(p2);
		permissionService.createPermission(p3);
		//2、新增角色
		r1 = new Role("admin", "管理员", Boolean.TRUE);
		r2 = new Role("user", "用户管理员", Boolean.TRUE);
		roleService.createRole(r1);
		roleService.createRole(r2);
		//3、关联角色-权限
		roleService.correlationPermissions(r1.getId(), p1.getId());
		roleService.correlationPermissions(r1.getId(), p2.getId());
		roleService.correlationPermissions(r1.getId(), p3.getId());

		roleService.correlationPermissions(r2.getId(), p1.getId());
		roleService.correlationPermissions(r2.getId(), p2.getId());

		//4、新增用户
		u1 = new User("zhang", defaultPassword);
		u2 = new User("li", defaultPassword);
		u3 = new User("wu", defaultPassword);
		u4 = new User("wang", defaultPassword);
		u4.setLocked(Boolean.TRUE);
		userService.createUser(u1);
		userService.createUser(u2);
		userService.createUser(u3);
		userService.createUser(u4);
		//5、关联用户-角色
		userService.correlationRoles(u1.getId(), r1.getId());
	}

	@After
	public void tearDown() throws Exception {
		ThreadContext.unbindSubject();//退出时请解除绑定Subject到线程 否则对下次测试造成影响
	}
	
	protected void login(String configFile, String username, String password) {
		//1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory(configFile);

		//2、得到SecurityManager实例 并绑定给SecurityUtils
		org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);

		//3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);

		subject.login(token);
	}

	protected void login(String username, String password) {
		// 得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);

		subject.login(token);
	}

	public Subject subject() {
		return SecurityUtils.getSubject();
	}

}