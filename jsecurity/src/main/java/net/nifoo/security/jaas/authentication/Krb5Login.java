package net.nifoo.security.jaas.authentication;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import com.sun.security.auth.callback.DialogCallbackHandler;
import com.sun.security.auth.callback.TextCallbackHandler;

public class Krb5Login {
	public static void main(String[] args) throws Exception {
		//登录
		//CallbackHandler handler = new TextCallbackHandler();
		CallbackHandler handler = new DialogCallbackHandler();
		LoginContext c = new LoginContext("krb5", handler);
		boolean pass;
		try {
			c.login();
			//登录成功
			pass = true;
		} catch (LoginException e) {
			//登录失败
			pass = false;
			System.err.println("Authentication failed:");
			// System.err.println("  " + e.getMessage());
			e.printStackTrace(System.err);
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
