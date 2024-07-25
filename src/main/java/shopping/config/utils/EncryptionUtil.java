package shopping.config.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class EncryptionUtil {
    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = "1c4d3f012e41b9de".getBytes(); // 반드시 16 바이트로 설정

    public static byte[] encrypt(String valueToEnc) {
        byte[] encValue;
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            encValue = c.doFinal(valueToEnc.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Error while encrypting: " + valueToEnc, e);
        }

        return encValue;
    }

    public static String decrypt(byte[] encryptedValue) {
        byte[] decValue;
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);
            decValue = c.doFinal(encryptedValue);
        } catch (Exception e) {
            throw new RuntimeException("Error while decrypting: " + encryptedValue, e);
        }

        return new String(decValue);
    }

    private static Key generateKey() throws Exception {
        return new SecretKeySpec(keyValue, ALGORITHM);
    }
}
