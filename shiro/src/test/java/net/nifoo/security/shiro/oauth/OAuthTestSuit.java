package net.nifoo.security.shiro.oauth;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	RoleTest.class, 
	PermissionTest.class, 
	AuthorizerTest.class 
})
public class OAuthTestSuit {

	//	public static Test suite() {
	//		TestSuite suite = new TestSuite();
	//		suite.addTest((new JUnit4TestAdapter(RoleTest.class)));
	//		suite.addTest((new JUnit4TestAdapter(PermissionTest.class)));
	//		suite.addTest((new JUnit4TestAdapter(AuthorizerTest.class)));
	//		return suite;
	//	}

}
