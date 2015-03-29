package net.nifoo.security.service;

import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-beans.xml", "/spring-shiro-web.xml" })
@ActiveProfiles("test")
public class UserRealmTest extends BaseTest {

	@Test
	public void testLoginSuccess() {
		login("zhang", defaultPassword);
		Assert.assertTrue(subject().isAuthenticated());
	}

	@Test(expected = UnknownAccountException.class)
	public void testLoginFailWithUnknownUsername() {
		login(u1.getUsername() + "1", defaultPassword);
	}

	@Test(expected = IncorrectCredentialsException.class)
	public void testLoginFailWithErrorPassowrd() {
		login(u1.getUsername(), defaultPassword + "1");
	}

	@Test(expected = LockedAccountException.class)
	public void testLoginFailWithLocked() {
		login(u4.getUsername(), defaultPassword + "1");
	}

	@Test(expected = ExcessiveAttemptsException.class)
	public void testLoginFailWithLimitRetryCount() {
		for (int i = 1; i <= 5; i++) {
			try {
				login(u3.getUsername(), defaultPassword + "1");
			} catch (Exception e) {/* ignore */
			}
		}
		
		login(u3.getUsername(), defaultPassword + "1");
	}

	@Test
	public void testHasRole() {
		login(u1.getUsername(), defaultPassword);
		Assert.assertTrue(subject().hasRole("admin"));
	}

	@Test
	public void testNoRole() {
		login(u2.getUsername(), defaultPassword);
		Assert.assertFalse(subject().hasRole("admin"));
	}

	@Test
	public void testHasPermission() {
		login(u1.getUsername(), defaultPassword);
		Assert.assertTrue(subject().isPermittedAll("user:create", "menu:create"));
	}

	@Test
	public void testNoPermission() {
		login(u2.getUsername(), defaultPassword);
		Assert.assertFalse(subject().isPermitted("user:create"));
	}

}
