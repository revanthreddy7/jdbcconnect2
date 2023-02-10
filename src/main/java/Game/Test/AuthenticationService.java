package Game.Test;
import Game.Test.User.UserServices;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

public class AuthenticationService {
    private static final String ALGORITHM = "AES";
    private static final byte[] KEY = "MySuperSecretKey".getBytes(StandardCharsets.UTF_8);


    // Authentication

    public static boolean Authenticate(String username, String password){
        try {
            return UserServices.checkUser(username, AuthenticationService.encrypt(password));
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // Validation

    /* Validation Codes
        1 = Username length should be greater than 3
        2 = Username already exists! Please use a different one
        3 = Please Include a minimum of 1 Digit 1 Alphabet in the Username
        4 = Passwords should have a minimum of 8 characters
        5 = Passwords do not match
        6 = Please enter the correct input
        7 = Validated
     */
    public static int Validate(String username, String pass, String repass, int currencyChoice){
        if(username.length()<3 ){
            return 1; // Username length should be greater than 3
        }else if(UserServices.getName() != null){
            return 2; // Username already exists! Please use a different one
        }
        boolean hasDigit = false;
        boolean hasAlphabet = false;

        for (int i = 0; i < username.length(); i++) {
            char f = username.charAt(i);

            if(Character.isDigit(f)) hasDigit = true;
            if(Character.isAlphabetic(f)) hasAlphabet = true;
        }
        if (!hasDigit || !hasAlphabet){
            return 3; // Please Include a minimum of 1 Digit 1 Alphabet in the Username
        }
        if (pass.length() <= 8) {
            return 4; // Passwords should have a minimum of 8 characters
        }else if (!pass.equals(repass)) {
            return 5; // Passwords do not match
        }
        if(currencyChoice < 1 || currencyChoice >5){
            return 6; //Please enter the correct input
        }
        return 7; // Validated
    }


    //Encryption
    public static String encrypt(String pwd) throws Exception{
        Key key = new SecretKeySpec(KEY, ALGORITHM);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);

        byte[] encValue = c.doFinal(pwd.getBytes());
        byte[] encryptedValue64 = Base64.getEncoder().encode(encValue);

        return new String(encryptedValue64);
    }

    //Decryption
    public static String decrypt(String encrypt) throws Exception{
        Key key = new SecretKeySpec(KEY, ALGORITHM);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);

        byte[] decodedValue64 = Base64.getDecoder().decode(encrypt.getBytes());
        byte[] decValue = c.doFinal(decodedValue64);

        return new String(decValue);
    }
}
