package Hospital.User.Service;

import java.security.MessageDigest;
import java.security.SecureRandom;

public class PWSecurity {
	//Salt 생성 <- 16비트
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
    //String 자료형 16진수로 변환 
    public static String Byte_to_String(byte[] temp) {
        StringBuilder sb = new StringBuilder();
        for (byte a : temp) {
            sb.append(String.format("%02x", a)); 
        }
        return sb.toString();
    }
    //웹으로 입력받은 패스워드와 회원가입 시 생성한 Salt를 결합하여 해싱
    public static String Hashing(byte[] password, byte[] salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        for (int i = 0; i < 10000; i++) {
            md.update(password);
            md.update(salt); 
            password = md.digest();
        }
        return Byte_to_String(password);
    }
}
