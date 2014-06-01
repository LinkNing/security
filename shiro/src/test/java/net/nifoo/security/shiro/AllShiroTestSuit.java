package net.nifoo.security.shiro;

import net.nifoo.security.shiro.authc.AuthenTestSuit;
import net.nifoo.security.shiro.codec.PasswordTestSuit;
import net.nifoo.security.shiro.oauth.OAuthTestSuit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)  
@Suite.SuiteClasses({   
AuthenTestSuit.class,   
OAuthTestSuit.class,
PasswordTestSuit.class
})  
public class AllShiroTestSuit {

}