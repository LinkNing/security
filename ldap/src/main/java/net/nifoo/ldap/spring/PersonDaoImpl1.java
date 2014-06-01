package net.nifoo.ldap.spring;

import static org.springframework.ldap.query.LdapQueryBuilder.*;

import java.util.List;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.AttributesMapperCallbackHandler;
import org.springframework.ldap.core.CollectingNameClassPairCallbackHandler;
import org.springframework.ldap.core.ContextExecutor;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.SearchExecutor;
import org.springframework.ldap.core.support.BaseLdapNameAware;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Repository;

/**
 * Basic Usage
 * @author Nifoo Ning
 *
 */
@Repository
public class PersonDaoImpl1 implements PersonDao, BaseLdapNameAware {
	final static Logger logger = LoggerFactory.getLogger(PersonDaoImpl1.class); 
	
	public static final String BASE_DN = "dc=example,dc=com";

	// a base LDAP path that be supplied to the ContextSource
	private LdapName basePath;

	private LdapTemplate ldapTemplate;

	public void setBaseLdapPath(LdapName basePath) {
		this.basePath = basePath;
	}

	@Autowired
	public void setContextSource(ContextSource contextSource) {
		this.ldapTemplate = new LdapTemplate(contextSource);
	}

	protected Person buildPerson(Name dn, Attributes attrs) {
		Person person = new Person();
		person.setCountry(LdapUtils.getStringValue(dn, "c"));
		person.setCompany(LdapUtils.getStringValue(dn, "ou"));
		person.setFullName(LdapUtils.getStringValue(dn, "cn"));
		// Populate rest of person object using attributes.

		return person;
	}

	public void create(Person p) {
		Name dn = buildDn(p);
		ldapTemplate.bind(dn, null, buildAttributes(p));
	}

	public void update(Person p) {
		Name dn = buildDn(p);
		ldapTemplate.rebind(dn, null, buildAttributes(p));
	}

	public void updateDescription(Person p) {
		Name dn = buildDn(p);
		Attribute attr = new BasicAttribute("description", p.getDescription());
		ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
		ldapTemplate.modifyAttributes(dn, new ModificationItem[] { item });
	}

	public void delete(Person p) {
		Name dn = buildDn(p);
		ldapTemplate.unbind(dn);
	}

	public List<Person> getAllPerson() {
		return ldapTemplate.search(query().where("objectclass").is("person"), new PersonAttributesMapper());
	}

	public List<Person> getAllPersonNames() {
		return ldapTemplate.search("", "(objectclass=person)", new PersonAttributesMapper());
	}

	public Person findPerson(String dn) {
		return ldapTemplate.lookup(dn, new PersonAttributesMapper());
	}

	public List<Person> getPersonNamesByLastName(String lastName) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "person"));
		filter.and(new EqualsFilter("sn", lastName));
		return ldapTemplate.search("", filter.encode(), new PersonAttributesMapper());
	}

	/**
	 * A custom search method using SearchExecutor and AttributesMapper
	 */
	public List search(final Name base, final String filter, final String[] params, final SearchControls ctls) {
		SearchExecutor executor = new SearchExecutor() {
			public NamingEnumeration executeSearch(DirContext ctx) throws NamingException {
				return ctx.search(base, filter, params, ctls);
			}
		};

		CollectingNameClassPairCallbackHandler handler = new AttributesMapperCallbackHandler(
				new PersonAttributesMapper());

		ldapTemplate.search(executor, handler);
		return handler.getList();
	}

	public Object lookupLink(final Name name) {
		ContextExecutor executor = new ContextExecutor() {
			public Object executeWithContext(DirContext ctx) throws NamingException {
				return ctx.lookupLink(name);
			}
		};

		return ldapTemplate.executeReadOnly(executor);
	}

	private Name buildDn(Person p) {
		return LdapNameBuilder.newInstance() //
				//.add("c", p.getCountry()) //
				//.add("ou", p.getCompany()) //
				.add("cn", p.getFullName()) //
				.build();
	}

	private Attributes buildAttributes(Person p) {
		Attributes attrs = new BasicAttributes();
		Attribute ocattr = new BasicAttribute("objectClass");
		ocattr.add("top");
		ocattr.add("person");
		attrs.put(ocattr);
		attrs.put("cn", p.getFullName());
		attrs.put("sn", p.getSn());
		return attrs;
	}

}

class PersonAttributesMapper implements AttributesMapper<Person> {
	public Person mapFromAttributes(Attributes attrs) throws NamingException {
		String cn = (String) attrs.get("cn").get();
		String sn = (String) attrs.get("sn").get();
		String desc = (String) attrs.get("description").get();
		return new Person(cn, sn, desc);
	}
}