package net.nifoo.ldap.spring;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("application-ldap.xml");

		System.out.println(">>>>>>>>>>>>>>>> search <<<<<<<<<<<<<<<<<<");
		PersonDao personDao = appContext.getBean(PersonDaoImpl2.class);
		List<Person> persons = personDao.getAllPersonNames();
		for (Person person : persons) {
			System.out.println(person.getSn());
		}
		System.out.println();

		System.out.println(">>>>>>>>>>>>>>>> bind <<<<<<<<<<<<<<<<<<");
		Person p = new Person("testCn", "testSn", "testDesc");
		p.setCountry("China");
		p.setCompany("ZhongFu");
		personDao.create(p);
		System.out.println();

		System.out.println(">>>>>>>>>>>>>>>> rebind <<<<<<<<<<<<<<<<<<");
		p.setSn("Rebind sn");
		personDao.update(p);
		System.out.println();

		System.out.println(">>>>>>>>>>>>>>>> Modifying using modifyAttributes <<<<<<<<<<<<<<<<<<");
		p.setDescription("this is for test");
		personDao.updateDescription(p);
		System.out.println();

		System.out.println(">>>>>>>>>>>>>>>> unbind <<<<<<<<<<<<<<<<<<");
		//personDao.delete(p);

		System.out.println(">>>>>>>>>>>>>>>> lookup <<<<<<<<<<<<<<<<<<");
		String dn = "uid=tomcat,ou=People";
		p = personDao.findPerson(dn);
		System.out.println(p.getCn());
		System.out.println();

		System.out.println(">>>>>>>>>>>>>>>> dynamic filter <<<<<<<<<<<<<<<<<<");
		persons = personDao.getPersonNamesByLastName("tomcat");
		for (Person person : persons) {
			System.out.println(person.getSn());
		}
		System.out.println();

	}

}
