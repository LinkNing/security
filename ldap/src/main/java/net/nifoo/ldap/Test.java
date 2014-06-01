package net.nifoo.ldap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
	final static Logger log = LoggerFactory.getLogger(Test.class);
	
	public static void main(String[] args) {
		log.info("System starting...");
		log.info(System.getProperty("log.dir"));
		log.info(System.getenv("USER_HOME"));
	}

}