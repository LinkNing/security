package net.nifoo.security.shiro.oauth;

import net.nifoo.security.shiro.BaseTest;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class AuthorizerTest extends BaseTest {

	@Test
	public void testIsPermitted() {
		login("classpath:shiro-oauth-authorizer.ini", "zhang", "123");

		//判断拥有权限：user:create
		Assert.assertTrue(getSubject().isPermitted("user1:create"));
		Assert.assertTrue(getSubject().isPermitted("user2:create"));

		//通过二进制位的方式表示权限
		Assert.assertTrue(getSubject().isPermitted("+user1+2"));//新增权限
		Assert.assertTrue(getSubject().isPermitted("+user1+8"));//查看权限
		Assert.assertTrue(getSubject().isPermitted("+user2+10"));//新增及查看

		Assert.assertFalse(getSubject().isPermitted("+user1+4"));//没有删除权限

		Assert.assertTrue(getSubject().isPermitted("menu:view"));//通过MyRolePermissionResolver解析得到的权限
	}

	@Ignore
	@Test
	public void testIsPermitted2() {
		login("classpath:shiro-oauth-jdbc-authorizer.ini", "zhang", "123");
		//判断拥有权限：user:create
		Assert.assertTrue(getSubject().isPermitted("user1:update"));
		Assert.assertTrue(getSubject().isPermitted("user2:update"));
		//通过二进制位的方式表示权限
		Assert.assertTrue(getSubject().isPermitted("+user1+2"));//新增权限
		Assert.assertTrue(getSubject().isPermitted("+user1+8"));//查看权限
		Assert.assertTrue(getSubject().isPermitted("+user2+10"));//新增及查看

		Assert.assertFalse(getSubject().isPermitted("+user1+4"));//没有删除权限

		Assert.assertTrue(getSubject().isPermitted("menu:view"));//通过MyRolePermissionResolver解析得到的权限
	}

}