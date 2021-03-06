package net.nifoo.security.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Before;

public abstract class BaseTest {

	@Before
	public void setUp() throws Exception {
		ThreadContext.unbindSubject(); // 开始时解除绑定到线程的Subject，排除其它测试对本次测试造成影响
		ThreadContext.unbindSecurityManager();
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
		ThreadContext.bind(securityManager);

		//3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);

		subject.login(token);
	}
	
	protected void login(String username, String password) {
		//得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);

		subject.login(token);
	}

	public Subject getSubject() {
		return SecurityUtils.getSubject();
	}
}