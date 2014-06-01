package net.nifoo.ldap.spring;

public class Person {

	private String country;

	private String company;

	private String fullName; // cn

	private String lastName; // sn

	private String description;

	private String[] roleNames;

	public Person() {
	}

	public Person(String cn, String sn, String description) {
		this.fullName = cn;
		this.lastName = sn;
	}

	public String getCn() {
		return fullName;
	}

	public void setCn(String cn) {
		this.fullName = cn;
	}

	public String getSn() {
		return lastName;
	}

	public void setSn(String sn) {
		this.lastName = sn;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String[] roleNames) {
		this.roleNames = roleNames;
	}

}
