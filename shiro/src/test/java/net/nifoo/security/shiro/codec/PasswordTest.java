package net.nifoo.security.shiro.codec;

import net.nifoo.security.shiro.BaseTest;

import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Ignore;
import org.junit.Test;

public class PasswordTest extends BaseTest {
	
	@Test
	public void testPasswordServiceWithMyRealm() {
		login("classpath:shiro-passwordservice.ini", "wu", "123");
	}

	@Test
	public void testGeneratePassword() {
		String algorithmName = "md5";
		String username = "liu";
		String password = "123";
		String salt1 = username;
		String salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();
		int hashIterations = 2;

		SimpleHash hash = new SimpleHash(algorithmName, password, salt1 + salt2, hashIterations);
		String encodedPassword = hash.toHex();
		System.out.println(salt2);
		System.out.println(encodedPassword);
	}

	@Test
	public void testHashedCredentialsMatcherWithMyRealm2() {
		//使用testGeneratePassword生成的散列密码
		login("classpath:shiro-hashedCredentialsMatcher.ini", "liu", "123");
	}

	@Test(expected = ExcessiveAttemptsException.class)
	@Ignore 
	public void testRetryLimitHashedCredentialsMatcherWithMyRealm() {
		// 初始化
		login("classpath:shiro-retryLimitHashedCredentialsMatcher.ini", "zhang", "123");
		
		// 重试5次
		for (int i = 1; i <= 5; i++) {
			try {
				login("zhang", "234");
			} catch (Exception e) {
				/*ignore*/
			}
		}

		login("zhang", "123"); // 即使密码正确，也会抛ExcessiveAttemptsException异常，因为超过重试次数
	}
}
