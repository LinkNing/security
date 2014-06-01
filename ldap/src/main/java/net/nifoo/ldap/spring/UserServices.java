package net.nifoo.ldap.spring;

import static org.springframework.ldap.query.LdapQueryBuilder.*;

import java.util.List;

import javax.naming.directory.DirContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

	final static Logger logger = LoggerFactory.getLogger(UserServices.class);

	private ContextSource contextSource;

	private LdapTemplate ldapTemplate;

	@Autowired
	public void setContextSource(ContextSource contextSource) {
		this.contextSource = contextSource;
		this.ldapTemplate = new LdapTemplate(contextSource);
	}

	public boolean authenticate(String userDn, String credentials) {
		//		ldapTemplate.authenticate(query().where("uid").is("nifoo"), "password");
		//		return true;

		DirContext ctx = null;
		try {
			ctx = contextSource.getContext(userDn, credentials); // 不能使用 client pool
			return true;
		} catch (Exception e) {
			// Context creation failed - authentication did not succeed
			logger.error("Login failed", e);
			return false;
		} finally {
			// It is imperative that the created DirContext instance is always closed
			LdapUtils.closeContext(ctx);
		}
	}

	private String getDnForUser(String uid) {
		List<String> result = ldapTemplate.search(query().where("uid").is(uid), new AbstractContextMapper() {
			protected String doMapFromContext(DirContextOperations ctx) {
				return ctx.getNameInNamespace();
			}
		});

		if (result.size() != 1) {
			throw new RuntimeException("User not found or not unique");
		}

		return result.get(0);
	}

	public static void main(String[] args) {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");

		UserServices userServices = appContext.getBean(UserServices.class);

		if (userServices.authenticate("uid=nifoo,ou=users,ou=system", "password")) {
			System.out.println("Pass");
		} else {
			System.out.println("invalida username or password!");
		}
	}
}
