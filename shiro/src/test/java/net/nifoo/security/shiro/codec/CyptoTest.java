package net.nifoo.security.shiro.codec;

import java.security.Key;

import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.BlowfishCipherService;
import org.apache.shiro.crypto.DefaultBlockCipherService;
import org.junit.Assert;
import org.junit.Test;

public class CyptoTest {
	@Test
	public void testAesCipherService() {
		AesCipherService aesCipherService = new AesCipherService();
		aesCipherService.setKeySize(128);//设置key长度

		//生成key
		Key key = aesCipherService.generateNewKey();

		String text = "hello";

		//加密
		String encrptText = aesCipherService.encrypt(text.getBytes(), key.getEncoded()).toHex();
		//解密
		String text2 = new String(aesCipherService.decrypt(Hex.decode(encrptText), key.getEncoded()).getBytes());

		Assert.assertEquals(text, text2);
	}

	@Test
	public void testBlowfishCipherService() {
		BlowfishCipherService blowfishCipherService = new BlowfishCipherService();
		blowfishCipherService.setKeySize(128);

		//生成key
		Key key = blowfishCipherService.generateNewKey();

		String text = "hello";

		//加密
		String encrptText = blowfishCipherService.encrypt(text.getBytes(), key.getEncoded()).toHex();
		//解密
		String text2 = new String(blowfishCipherService.decrypt(Hex.decode(encrptText), key.getEncoded()).getBytes());

		Assert.assertEquals(text, text2);
	}

	@Test
	public void testDefaultBlockCipherService() throws Exception {

		//对称加密，使用Java的JCA（javax.crypto.Cipher）加密API，常见的如 'AES', 'Blowfish'
		DefaultBlockCipherService cipherService = new DefaultBlockCipherService("AES");
		cipherService.setKeySize(128);

		//生成key
		Key key = cipherService.generateNewKey();

		String text = "hello";

		//加密
		String encrptText = cipherService.encrypt(text.getBytes(), key.getEncoded()).toHex();
		//解密
		String text2 = new String(cipherService.decrypt(Hex.decode(encrptText), key.getEncoded()).getBytes());

		Assert.assertEquals(text, text2);
	}

	//加解密相关知识可参考snowolf的博客 http://snowolf.iteye.com/category/68576
}
