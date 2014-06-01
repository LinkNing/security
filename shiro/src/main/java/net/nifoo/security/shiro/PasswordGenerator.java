package net.nifoo.security.shiro;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

public class PasswordGenerator {

	public static void main(String[] args) {
		String algorithmName = "md5";
		String username = "liu";
		String password = "123";
		String salt1 = username;
		String salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();
		System.out.println(salt2);

		int hashIterations = 2;
		SimpleHash hash = new SimpleHash(algorithmName, password, salt1 + salt2, hashIterations);

		System.out.println(hash);
	}

}
