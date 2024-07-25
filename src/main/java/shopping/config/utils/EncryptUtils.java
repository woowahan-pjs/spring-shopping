package shopping.config.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

@Slf4j
@Component
public class EncryptUtils {
    // 고정된 AES 키 (16 바이트, 128 비트)
    private static final String FIXED_KEY = "1234567890123456"; // 16자 문자열, 128비트 키

    public static String encrypt(String text) {

        SecretKeySpec secretKey = new SecretKeySpec(FIXED_KEY.getBytes(), "AES");

        byte[] encryptedBytes;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedBytes = cipher.doFinal(text.getBytes());

        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException("암호화 처리중에 에러가 발생했습니다. e = {}");
        }
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encodedValue) {
        SecretKeySpec secretKey = new SecretKeySpec(FIXED_KEY.getBytes(), "AES");

        byte[] decryptedBytes;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // 암호문 복호화
            byte[] decodedBytes = Base64.getDecoder().decode(encodedValue);
            decryptedBytes = cipher.doFinal(decodedBytes);
        } catch (Exception e) {
            throw new RuntimeException("복호화 처리중에 에러가 발생했습니다. e = {}");
        }

        return new String(decryptedBytes);
    }

}
