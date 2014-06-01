package net.nifoo.security.jaas;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleLoginModule implements LoginModule {
	public static final Logger log = LoggerFactory.getLogger(SimpleLoginModule.class);

	private Subject subject;

	private CallbackHandler callbackHandler;

	private Map sharedState;

	private Map options;

	private String debug;

	private boolean isAuthenticated;

	private Principal principal;

	/**
	 * 这个方法的目的就是用有关的信息去实例化这个LoginModule。如果login成功，在这个方法里的Subject就被用在存储Principals和Credentials.  注意这个方法有一个能被用作输入认证信息的CallbackHandler。
	 */
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {

		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.sharedState = sharedState;
		this.options = options;

		debug = (String) this.options.get("debug");
	}

	public boolean login() throws LoginException {

		Callback[] callbacks = new Callback[2];
		callbacks[0] = new NameCallback("用户名: ");
		callbacks[1] = new PasswordCallback("密码: ", false);

		try {

			callbackHandler.handle(callbacks);
			String userName = ((NameCallback) callbacks[0]).getName();
			char[] password = ((PasswordCallback) callbacks[1]).getPassword();

			if (debug.equals("true")) {
				log.debug("你输入的用户名为:" + userName);
				log.debug("你输入的密码为:" + new String(password));
			}

			// TODO 验证，如：查询数据库、LDAP。。。

			if (userName.equals("nifoo") && new String(password).equals("password")) {
				log.info("验证成功");
				principal = new SimplePrincipal(userName);
				isAuthenticated = true;
			} else {
				log.info("验证失败");
				userName = null;
				password = null;
			}

		} catch (IOException e) {
			throw new LoginException("no such user");
		} catch (UnsupportedCallbackException e) {
			throw new LoginException("login failure");
		}

		return isAuthenticated;
	}

	/**
	 * 通知其他LoginModule供应者或LoginModule模型认证已经失败了。整个login将失败。
	 */
	public boolean abort() throws LoginException {
		log.debug("abort()");
		return false;
	}

	/**
	   * 如果LoginContext的认证全部成功就调用这个方法。
	   * 在Subject中加入用户对象.
	   */
	public boolean commit() throws LoginException {
		log.debug("commit()");
		
		if (isAuthenticated) {
			subject.getPrincipals().add(principal);
		} else {
			throw new LoginException("Authentication failure");
		}
		
		return isAuthenticated;
	}

	/**
	 * 通过从Subject里移除Principals和Credentials注销Subject。
	 */
	public boolean logout() throws LoginException {
		log.debug("logout()");
		
		subject.getPrincipals().remove(principal);
		return false;
	}

}
