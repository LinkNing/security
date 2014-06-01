package net.nifoo.ldap.spring;

import javax.naming.Name;
import javax.naming.ldap.LdapName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapNameAware;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Repository;

/**
 * DirContextAdapter and Distinguished Names as Attribute Values.
 * 
 * When managing security groups in LDAP it is very common to have attribute values that represent distinguished names. 
 * Since distinguished name equality differs from String equality (e.g. whitespace and case differences are ignored in 
 * distinguished name equality), calculating attribute modifications using string equality will not work as expected.
 * 
 * For instance, if a member attribute has the value cn=John Doe,ou=People and we call ctx.addAttributeValue("member", "CN=John Doe, OU=People"), 
 * the attribute will now be considered to have two values, even though the strings actually represent the same distinguished name.
 * 
 * As of Spring LDAP 2.0, supplying javax.naming.Name instances to the attribute modification methods will make DirContextAdapter
 *  use distinguished name equality when calculating attribute modifications. If we modify the example above to: 
 *  ctx.addAttributeValue("member", LdapUtils.newLdapName("CN=John Doe, OU=People")), this will not render a modification.
 * 
 * @author Nifoo Ning
 *
 */
@Repository
class GroupRepo implements BaseLdapNameAware {
	private LdapTemplate ldapTemplate;
	private LdapName baseLdapPath;

	@Autowired
	public void setContextSource(ContextSource contextSource) {
		this.ldapTemplate = new LdapTemplate(contextSource);
	}

	public void setBaseLdapPath(LdapName baseLdapPath) {
		this.baseLdapPath = baseLdapPath;
	}

	public void addMemberToGroup(String groupName, Person person) {
		Name groupDn = buildGroupDn(groupName);
		Name userDn = buildPersonDn(person.getFullName(), person.getCompany(), person.getCountry());

		DirContextOperations ctx = ldapTemplate.lookupContext(groupDn);
		ctx.addAttributeValue("member", userDn);

		ldapTemplate.update(ctx);
	}

	public void removeMemberFromGroup(String groupName, Person person) {
		Name groupDn = buildGroupDn(groupName);
		Name userDn = buildPersonDn(person.getFullName(), person.getCompany(), person.getCountry());

		DirContextOperations ctx = ldapTemplate.lookupContext(groupDn);
		ctx.removeAttributeValue("member", userDn);

		ldapTemplate.update(ctx);
	}

	private Name buildGroupDn(String groupName) {
		return LdapNameBuilder.newInstance("ou=Groups").add("cn", groupName).build();
	}

	private Name buildPersonDn(String fullname, String company, String country) {
		return LdapNameBuilder.newInstance(baseLdapPath).add("c", country).add("ou", company).add("cn", fullname)
				.build();
	}
}