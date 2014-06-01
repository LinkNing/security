package net.nifoo.security.jaas;

import java.security.PrivilegedAction;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

public class SimpleLogin {

	public static void main(String[] args) {
		// 建立登陆上下文，并通过配置文件初始化，在这里配置文件必须与程序同目录     
		LoginContext loginContext = null;
		try {
			//此处指定了使用配置文件的“simple”验证模块，对应的实现类为SimpleLoginModule
			loginContext = new LoginContext("simple", new SimpleCallbackHandle());
		} catch (LoginException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		try {
			// 如果不抛出异常表示验证成功     
			loginContext.login();
		} catch (LoginException e) {
			e.printStackTrace();
		}

		Subject subject = loginContext.getSubject();
		PrivilegedAction action = new SimpleAction();
		Subject.doAsPrivileged(subject, action, null);

		try {
			loginContext.logout();
		} catch (LoginException e) {
			System.out.println("Logout: " + e.getMessage());
		}
	}

}