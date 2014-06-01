package net.nifoo.security.jaas;

import java.security.Principal;

public final class SimplePrincipal implements Principal {

	private String name;

	public SimplePrincipal(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean equals(Object o) {
		return (o instanceof SimplePrincipal) && this.name.equalsIgnoreCase(((SimplePrincipal) o).name);
	}

	public int hashCode() {
		return name.toUpperCase().hashCode();
	}

}