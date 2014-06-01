package net.nifoo.ldap.spring;

import java.util.List;

public interface PersonDao {

	public void create(Person p);

	public void update(Person p);

	public void updateDescription(Person p);

	public void delete(Person p);

	public List<Person> getAllPersonNames();

	public Person findPerson(String dn);

	public List<Person> getPersonNamesByLastName(String lastName);

}