import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;


public class Main {


    static Scanner sc;
    static Connection con;
    static PreparedStatement st ;

    static Statement statement;
    static ResultSet rs;
    private static final String ALGORITHM = "AES";
    private static final byte[] KEY = "MySuperSecretKey".getBytes(StandardCharsets.UTF_8);

    public static final String connectionUrl  = "jdbc:sqlserver://localhost:1433;" + "databaseName=game;" + "user=sa; password=Password@10;" + " encrypt=true;trustServerCertificate=true";
    public static void main(String[] args) {

        sc = new Scanner(System.in);

        System.out.println("Enter 1 if you are a new user : ");
        System.out.println("Enter 2 if you are existing user : ");

        int choice = sc.nextInt();
        if (choice==1){
            try {
                input();
            }
            catch (Exception e){
                System.err.println(e);
            }
        } else if (choice==2) {

            try {
                authUser();
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

    //User Input
    public static void input(){
        int amt = 0;
        int choice;

        String name = "";
        String username = "";
        String pass = "";
        String repass = "";
        String currency = "";

        try{

            System.out.println("Enter the name: ");
            name = sc.next();

            System.out.println("Enter the username: ");
            username = sc.next();

            System.out.println("Enter the Password: ");
            pass = sc.next();

            System.out.println("Enter the Password again: ");
            repass = sc.next();

            System.out.println("Enter the wallet Amount: ");
            amt = sc.nextInt();

            System.out.println("Enter Your Choice of Currency:1:EURO, 2:USD, 3:INR, 4:POUND, 5:WON");
            choice = sc.nextInt();
            ArrayList<String> currencies = new ArrayList<>(
                    Arrays.asList("EURO", "USD", "INR", "POUND", "WON")
            );

            currency = currencies.get(choice);

            if(choice<1 || choice>5){
                System.out.println("Please enter the correct input!!");
                input();
            }
        }
        catch (Exception e){
            System.err.println(e);
            input();
        }
//
        insertData(username, name, pass, repass, amt, currency);
    }

    //User Authentication
    public static void  authUser() throws Exception{
        System.out.print("Enter username : ");
//        sc.next();
        String username = sc.next() ;
//        sc.next();
        con = DriverManager.getConnection(connectionUrl);
        Statement statement = con.createStatement() ;
        String query = "SELECT name FROM userdetails WHERE username = '" + username+ "'" ;

        ResultSet resultSetUsername = statement.executeQuery(query) ;

        //System.out.print( query );

        if ( resultSetUsername.next() ){
            int queryCount = 0 ;
            ResultSet resultSetAuthentication = null ;
            boolean passwordMatched = false ;

            do{

                System.out.print("Enter Password : " );
                String password = sc.next() ;

                resultSetAuthentication = statement.executeQuery("SELECT name , wallet_amt FROM userdetails WHERE username = '" + username+ "' AND password = '"+ encrypt(password) + "'" ) ;

                if( ! resultSetAuthentication.next() ){
                    System.out.print("Wrong password Enter password again \n");
                    passwordMatched = false ;
                }else{
                    passwordMatched = true ;
                }

                queryCount++ ;

            }while(queryCount < 3 && ! passwordMatched ) ;


            if( passwordMatched ){

                String name = resultSetAuthentication.getString(1) ;
                double wallet_amount = resultSetAuthentication.getDouble( 2 ) ;

                displayAll(name ,  wallet_amount, username);

            }else {
                System.out.print("You exceeded password limit \n");

            }

        }else{

            System.out.print("No such username exists. Create an Account ");
        }
    }

    // Validation
    public static Boolean validation(String username, String password, String retype_password) {

        boolean hasDigit = false;
        boolean hasAlphabet = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char f = password.charAt(i);

            if(Character.isDigit(f)) hasDigit = true;
            if(Character.isAlphabetic(f)) hasAlphabet = true;
            if(!Character.isDigit(f) && !Character.isAlphabetic(f)) hasSpecial = true;
        }

        if(username.length()<3){
            System.out.println("Username length should be greater than 3");
            return false;
        } else if (password.length() < 8) {
            System.out.println("Passwords should have a minimum of 8 characters");
            return false;
        } else if ( !hasDigit || !hasAlphabet || !hasSpecial){
            System.out.println("Please Include a minimum of 1 Digit 1 Alphabet and 1 Special Character");
            return false;
        } else if (!password.equals(retype_password)) {
            System.out.println("Passwords do not match");
            return false;
        }

        return true;
    }

    //Inserting into table
    public static Boolean insertData(String username, String name, String pass, String repass, double amt, String currency){
        try{

            if(validation(username, pass, repass)){

                String Query = "INSERT INTO userdetails VALUES (?,?,?,?,?)";
                con = DriverManager.getConnection(connectionUrl);
                st = con.prepareStatement(Query);


                int userid = 0 ;
                //Add userid auto increment-------------------

                st.setInt(1, userid);
                st.setString(2, username);
                st.setString(3, name);
                st.setString(4, encrypt(pass));
                st.setDouble(5, getBalance(amt, currency));

                int rowsEffected = st.executeUpdate();

                if(rowsEffected == 0){
                    return false;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("The UserName Already Exists");
            insertData(username, name, pass, repass, amt, currency);
        }
        return true;
    }

    //Currency Conversion
    static double getBalance(double wallet_amt , String currency){

        if( currency.equalsIgnoreCase("USD") )return wallet_amt * 0.92 ;
        else if( currency.equalsIgnoreCase("INR") )return wallet_amt * 0.011 ;
        else if( currency.equalsIgnoreCase("POUND") )return wallet_amt * 0.0071 ;
        else if( currency.equalsIgnoreCase("WON") )return wallet_amt * 0.00075 ;

        return wallet_amt ;

    }

    //Encryption
    public static String encrypt(String pwd) throws Exception{
        Key key = new SecretKeySpec(KEY, ALGORITHM);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);

        byte[] encValue = c.doFinal(pwd.getBytes());
        byte[] encryptedValue64 = Base64.getEncoder().encode(encValue);
        String sd =  new String(encryptedValue64);
        System.out.println(sd);
        return sd;
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

    //Displaying User Information
    public static void displayAll(String name, double wallet_amount, String username){
        System.out.println("***-------****-------***");
        System.out.print("Name          : " + name + "\n" ) ;
        System.out.print("Username      : " + username + "\n" ) ;
        System.out.print("Wallet Amount : " + wallet_amount + "\n" ) ;
        System.out.println("***-------****-------***");
    }
}
