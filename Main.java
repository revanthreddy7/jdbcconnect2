import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.Scanner;


public class Main {


    static Scanner sc = null;
    static Connection con = null;
    static PreparedStatement p = null;
    static ResultSet r = null;
    private static final String ALGORITHM = "AES";
    private static final byte[] KEY = "MySuperSecretKey".getBytes(StandardCharsets.UTF_8);
    public static void main(String[] args) {

        sc = new Scanner(System.in);
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=game;user=sa;password=Prevanth123;encrypt=true;trustServerCertificate=true;  ";


        System.out.println("Enter 1 if you are a new user : ");
        System.out.println("Enter 2 if you are existing user : ");

        int choice = sc.nextInt();
        if (choice==1){
            login();
            try {
                con = DriverManager.getConnection(connectionUrl);

            }
            catch (Exception e){
                System.err.println(e);
            }
        } else if (choice==2) {
            input();
            try {
                con = DriverManager.getConnection(connectionUrl);

            }
            catch (Exception e){
                System.err.println(e);
            }

        }
        else{
            System.out.println("Enter the correct value!!!");
            main(new String[]{"args"});
        }


    }

    public static void input(){
        System.out.println("Enter the name: ");
        String name = sc.next();
        System.out.println("Enter the username: ");
        String username = sc.next();
        System.out.println("Enter the Password: ");
        String pass = sc.next();
        System.out.println("Enter the Password again: ");
        String repass = sc.next();
        try{
            System.out.println("Enter the wallet Amount: ");
            int amt = sc.nextInt();
        }
        catch (Exception e){
            System.err.println(e);
        }

        createacc(name, username, pass, repass, amt);
    }

    public static String EncPwd(String pwd, byte[] KEY, String alg) throws Exception{

        Key key = new SecretKeySpec(KEY, alg);

        Cipher cipher = Cipher.getInstance(alg);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encValue = cipher.doFinal(pwd.getBytes());
        byte[] encryptedValue64 = Base64.getEncoder().encode(encValue);
        String pass =  new String(encryptedValue64);
        System.out.println(pass);

        return pass;

    }

    public static String Encrypt(String pwd) throws Exception{
        Key key = new SecretKeySpec(KEY, ALGORITHM);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);

        byte[] encValue = c.doFinal(pwd.getBytes());
        byte[] encryptedValue64 = Base64.getEncoder().encode(encValue);
        String sd =  new String(encryptedValue64);
        System.out.println(sd);
        return sd;
    }

    public static String Decrypt(String encrypt) throws Exception{
        Key key = new SecretKeySpec(KEY, ALGORITHM);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);

        byte[] decodedValue64 = Base64.getDecoder().decode(encrypt.getBytes());
        byte[] decValue = c.doFinal(decodedValue64);
        return new String(decValue);

    }

    public static void displayAll(String name, int wallet_amt, String username){
        System.out.println("***-------****-------***");
        System.out.println(username);
        System.out.println(name);
        System.out.println(wallet_amt);
        System.out.println("***-------****-------***");

    }
}