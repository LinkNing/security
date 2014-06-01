package net.nifoo.ldap.jndi;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class AccessActiveDirectory {

	public static void main(String[] args) throws Exception {
		AccessActiveDirectory je = new AccessActiveDirectory();

		DirContext ctx = je.getContext();

		//		String base = "dc=nifoo,dc=com";
		String base = "dc=example,dc=com";
		String filter = "(objectClass=person)";
		//		String filter = "(&(objectClass=person)(uid=user*))";
		String[] attributes = new String[] { "uid", "userPassword", "mail" };
		je.search(ctx, base, SearchControls.SUBTREE_SCOPE, filter, attributes);

		//		String base = "ou=system";
		//		String filter = "(objectClass=*)";
		//		String[] attributes = new String[] { "uid", "mail" };
		//		je.search(ctx, base, SearchControls.ONELEVEL_SCOPE, filter, attributes);

		ctx.close();
	}

	public DirContext getContext() throws NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>();

		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://localhost:10389");

		env.put(Context.SECURITY_PRINCIPAL, "uid=admin,ou=system");
		env.put(Context.SECURITY_CREDENTIALS, "secret");

		//		env.put(Context.SECURITY_PRINCIPAL, "Administrator@nifoo.com");
		//		env.put(Context.SECURITY_CREDENTIALS, "ningfeng");
		//		env.put(Context.SECURITY_PRINCIPAL, "uid=nifoo,ou=users,ou=system");
		//		env.put(Context.SECURITY_CREDENTIALS, "ningfeng");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");

		DirContext ctx = new InitialDirContext(env);

		//		Attributes attrs = ctx.getAttributes("");
		//		NamingEnumeration enm = attrs.getAll();
		//		while (enm.hasMore()) {
		//			System.out.println(enm.next());
		//		}

		return ctx;
	}

	public void search(DirContext ctx, String base, int scope, String filter, String[] attributes)
			throws NamingException {
		SearchControls ctls = new SearchControls();
		ctls.setSearchScope(scope);
		ctls.setReturningAttributes(attributes);

		NamingEnumeration<SearchResult> resultEnum = ctx.search(base, filter, ctls);
		while (resultEnum.hasMore()) {
			SearchResult result = resultEnum.next();

			// print DN of entry
			System.out.println(result.getNameInNamespace());

			// print attributes returned by search
			Attributes attrs = result.getAttributes();
			NamingEnumeration e = attrs.getAll();
			while (e.hasMore()) {
				Attribute attr = (Attribute) e.next();
				System.out.println(attr);
			}
			System.out.println();
		}

	}

}
