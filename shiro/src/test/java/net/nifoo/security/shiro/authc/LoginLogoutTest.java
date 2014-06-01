package net.nifoo.security.shiro.authc;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.junit.Before;
import org.junit.Test;

public class LoginLogoutTest {

	@Before
	public void tearDown() {
		ThreadContext.unbindSubject();
		ThreadContext.unbindSecurityManager();
		SecurityUtils.setSecurityManager(null);
	}

	@Test
	public void testHelloworld() {
		//1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-simple.ini");

		//2、得到SecurityManager实例并绑定给SecurityUtils
		org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);

		//3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
		try {
			//4、登录，即身份验证
			subject.login(token);
		} catch (AuthenticationException e) {
			//5、身份验证失败
			e.printStackTrace();
		}

		assertEquals(true, subject.isAuthenticated()); //断言用户已经登录

		//6、退出
		subject.logout();
	}

	@Test
	public synchronized void testCustomRealm() {
		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory(
				"classpath:shiro-realm.ini");
		org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);

		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = null;

		token = new UsernamePasswordToken("zhang", "123");
		subject.login(token);
		assertTrue(subject.isAuthenticated()); //断言用户已经登录
		subject.logout();

		token = new UsernamePasswordToken("wang", "123");
		try {
			subject.login(token);
			fail("realm1 不能验证用户 wang, 但是他通过了验证。");
		} catch (AuthenticationException e) {
			assertThat(e.getMessage(), is("wang is not exist!"));
		}
		assertFalse(subject.isAuthenticated()); //断言用户不能登录
		subject.logout();
	}

	@Test
	public synchronized void testCustomMultiRealm() {
		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory(
				"classpath:shiro-multi-realm.ini");
		org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);

		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = null;

		token = new UsernamePasswordToken("zhang", "123");
		subject.login(token);
		assertTrue(subject.isAuthenticated()); //断言用户已经登录
		subject.logout();

		token = new UsernamePasswordToken("wang", "123");
		subject.login(token);
		assertTrue(subject.isAuthenticated()); //断言用户已经登录
		subject.logout();
	}
}