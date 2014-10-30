package net.nifoo.security.jaas.authentication;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

public class NTLogin {
	public static void main(String[] args) throws Exception {
		//登录
		LoginContext c = new LoginContext("ntlm");
		boolean pass;
		try {
			c.login();
			//登录成功
			pass = true;
		} catch (LoginException le) {
			//登录失败
			pass = false;
			System.err.println("Authentication failed:");
			System.err.println("  " + le.getMessage());
		}
		//显示登录结果
		if (!pass) {
			System.out.println("Sorry");
		} else {
			System.out.println("Authentication succeeded!");
			Subject s = c.getSubject();
			System.out.println(s.getPrincipals());
		}
	}
}
