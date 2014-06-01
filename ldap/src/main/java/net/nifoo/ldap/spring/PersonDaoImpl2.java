package net.nifoo.ldap.spring;

import java.util.List;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.CollectingNameClassPairCallbackHandler;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.ContextMapperCallbackHandler;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.SearchExecutor;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Repository;

/**
 * Simplifying Attribute Access and Manipulation with DirContextAdapter
 * 
 * A little-known — and probably underestimated — feature of the Java LDAP API is the ability to register a DirObjectFactory 
 * to automatically create objects from found LDAP entries. Spring LDAP makes use of this feature to return DirContextAdapter 
 * instances in certain search and lookup operations.
 * 
 * DirContextAdapter is a very useful tool for working with LDAP attributes, particularly when adding or modifying data.
 * 
 * @author Nifoo Ning
 *
 */
@Repository
public class PersonDaoImpl2 implements PersonDao {
	public static final String BASE_DN = "dc=example,dc=com";

	private LdapTemplate ldapTemplate;

	@Autowired
	public void setContextSource(ContextSource contextSource) {
		this.ldapTemplate = new LdapTemplate(contextSource);
	}

	public void create(Person p) {
		Name dn = buildDn(p);
		DirContextAdapter context = new DirContextAdapter(dn);
		// context.setAttributeValues("objectclass", new String[] { "top", "person" });
		mapToContext(p, context);
		ldapTemplate.bind(context);
	}

	public void update(Person p) {
		Name dn = buildDn(p);
		DirContextAdapter context = (DirContextAdapter) ldapTemplate.lookupContext(dn);
		mapToContext(p, context);
		ldapTemplate.rebind(dn, context, null);
	}

	public void updateDescription(Person p) {
		Name dn = buildDn(p);
		DirContextOperations context = ldapTemplate.lookupContext(dn);
		mapToContext(p, context);
		ldapTemplate.modifyAttributes(context);
	}

	public void delete(Person p) {
		Name dn = buildDn(p);
		ldapTemplate.unbind(dn);
	}

	public Person findPerson(String dn) {
		return ldapTemplate.lookup(dn, new PersonContextMapper());
	}

	public List<Person> getAllPersonNames() {
		return ldapTemplate.search("", "(objectclass=person)", new PersonContextMapper());
	}

	public List<Person> getPersonNamesByLastName(String lastName) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectClass", "person"));
		filter.and(new EqualsFilter("sn", lastName));

		return ldapTemplate.search("", filter.encode(), new PersonContextMapper());
	}

	public List<Person> findByName(String name) {
		LdapQuery query = LdapQueryBuilder.query().where("objectclass").is("person").and("cn")
				.whitespaceWildcardsLike("name");

		return ldapTemplate.search(query, new PersonContextMapper());
	}

	public List<Person> findAll() {
		EqualsFilter filter = new EqualsFilter("objectclass", "person");
		return ldapTemplate.search(LdapUtils.emptyLdapName(), filter.encode(), new PersonContextMapper());
	}

	/**
	 * A custom search method using SearchExecutor and ContextMapper
	 */
	public List search(final Name base, final String filter, final String[] params, final SearchControls ctls) {
		SearchExecutor executor = new SearchExecutor() {
			public NamingEnumeration executeSearch(DirContext ctx) throws NamingException {
				return ctx.search(base, filter, params, ctls);
			}
		};

		CollectingNameClassPairCallbackHandler handler = new ContextMapperCallbackHandler(new PersonContextMapper());

		ldapTemplate.search(executor, handler);
		return handler.getList();
	}

	protected ContextMapper<Person> getContextMapper() {
		return new PersonContextMapper();
	}

	protected Name buildDn(Person p) {
		return LdapNameBuilder.newInstance() //
				//.add("c", p.getCountry()) //
				//.add("ou", p.getCompany()) //
				.add("cn", p.getFullName()) //
				.build();

		//		Name dn = null;
		//		try {
		//			dn = new LdapName(BASE_DN);
		//			dn.add("c=" + p.getCountry());
		//			dn.add("ou=" + p.getCompany());
		//			dn.add("cn=" + p.getFullName());
		//		} catch (InvalidNameException e) {
		//			throw new IllegalArgumentException(e);
		//		}
		//		return dn;
	}

	protected void mapToContext(Person p, DirContextOperations context) {
		context.setAttributeValues("objectClass", new String[] { "top", "person" });
		context.setAttributeValue("cn", p.getFullName());
		context.setAttributeValue("sn", p.getLastName());
		context.setAttributeValue("description", p.getDescription());
	}

}

/**
 * As shown below, we can retrieve the attribute values directly by name without having to go through the Attributes
 *   and Attribute classes. This is particularly useful when working with multi-value attributes. Extracting values 
 *   from multi-value attributes normally requires looping through a NamingEnumeration of attribute values returned 
 *   from the Attributes implementation. DirContextAdapter does this for you in the getStringAttributes() 
 *   or getObjectAttributes() methods
 *   
 * @author Nifoo Ning
 *
 */
class PersonContextMapper implements ContextMapper<Person> {

	public Person mapFromContext(Object ctx) {
		DirContextAdapter context = (DirContextAdapter) ctx;
		Person p = new Person();
		p.setFullName(context.getStringAttribute("cn"));
		p.setLastName(context.getStringAttribute("sn"));
		p.setDescription(context.getStringAttribute("description"));

		// The roleNames property of Person is an String array
		p.setRoleNames(context.getStringAttributes("roleNames"));
		return p;
	}

}

/**
 * 此类和PersonContextMapper类实现同样功能，但是继承AbstractContextMapper，代码更简洁。
 * 
 * @author Nifoo Ning
 *
 */
class PersonContextMapper2 extends AbstractContextMapper<Person> {

	public Person doMapFromContext(DirContextOperations context) {
		Person p = new Person();
		p.setFullName(context.getStringAttribute("cn"));
		p.setLastName(context.getStringAttribute("sn"));
		p.setDescription(context.getStringAttribute("description"));
		return p;
	}

}