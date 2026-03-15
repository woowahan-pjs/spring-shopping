package shopping.util;

import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {

	private static final String SECRET_KEY = "shopping-secret-key-1234-5678-90"; // 프로퍼티로 주입시켜야 하지만 과제 수행 목적으로 코드에 남겨둠
	private static final String ALGORITHM = "AES";

	public static String encrypt(String plainText) {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			byte[] encrypted = cipher.doFinal(plainText.getBytes());
			return Base64.getEncoder().encodeToString(encrypted);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String encrypt(String plainText, String salt) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] derivedKey = digest.digest((SECRET_KEY + salt).getBytes());
			SecretKeySpec keySpec = new SecretKeySpec(derivedKey, ALGORITHM);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			byte[] encrypted = cipher.doFinal(plainText.getBytes());
			return Base64.getEncoder().encodeToString(encrypted);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String decrypt(String encryptedText) {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			byte[] decoded = Base64.getDecoder().decode(encryptedText);
			return new String(cipher.doFinal(decoded));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}