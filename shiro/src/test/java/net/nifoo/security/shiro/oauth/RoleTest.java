package net.nifoo.security.shiro.oauth;

import static org.junit.Assert.*;

import java.util.Arrays;

import net.nifoo.security.shiro.BaseTest;

import org.apache.shiro.authz.UnauthorizedException;
import org.junit.Test;

public class RoleTest extends BaseTest {

	@Test
	public void testHasRole() {
		login("classpath:shiro-oauth-role.ini", "zhang", "123");

		//判断拥有角色：role1
		assertTrue(getSubject().hasRole("role1"));
		//判断拥有角色：role1 and role2
		assertTrue(getSubject().hasAllRoles(Arrays.asList("role1", "role2")));
		//判断拥有角色：role1 and role2 and !role3
		boolean[] result = getSubject().hasRoles(Arrays.asList("role1", "role2", "role3"));

		assertEquals(true, result[0]);
		assertEquals(true, result[1]);
		assertEquals(false, result[2]);
	}

	/**
	 * Shiro 提供的checkRole/checkRoles 和hasRole/hasAllRoles 不同的地方是它在判断为假的情
	 * 况下会抛出UnauthorizedException异常。
	 */
	@Test(expected = UnauthorizedException.class)
	public void testCheckRole() {
		login("classpath:shiro-oauth-role.ini", "zhang", "123");
		//断言拥有角色：role1
		getSubject().checkRole("role1");
		//断言拥有角色：role1 and role3 失败抛出异常
		getSubject().checkRoles("role1", "role3");
	}
}
