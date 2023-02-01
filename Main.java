import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Scanner;
import java.util.*;

public class Main{

    static Scanner sc;
    static Connection con;
    static PreparedStatement st ;

    static Statement statement;
    static ResultSet rs;
    private static final String ALGORITHM = "AES";
    private static final byte[] KEY = "MySuperSecretKey".getBytes(StandardCharsets.UTF_8);

    public static final String connectionUrl  = "jdbc:sqlserver://localhost:1433;" + "databaseName=game;" + "user=sa; password=Prevanth123;" + " encrypt=true;trustServerCertificate=true";
    public static void main(String[] args) throws SQLException {

        sc = new Scanner(System.in);

        System.out.println("Enter 1 if you are a new user : ");
        System.out.println("Enter 2 if you are existing user : ");
//        System.out.println("Enter 3 for testing : ");


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
//        else if (choice==3){
//            System.out.println("Enter Number of rows to be tested: ");
//            Scanner sc = new Scanner(System.in);
//            int rows = sc.nextInt();
//
//            long start;
//            long end;
//            long execution;
//
//            // get the start time
//            start = System.currentTimeMillis();
//
//            // call the method
//            batchEnter1(rows);
//
//            // get the end time
//            end = System.currentTimeMillis();
//
//            // execution time
//            execution = end - start;
//
//            System.out.println("Execution time with trigger : " + execution);
//
//            // get the start time
//            start = System.currentTimeMillis();
//
//            // call the method
//            batchEnter2(rows);
//
//            // get the end time
//            end = System.currentTimeMillis();
//
//            // execution time
//            execution = end - start;
//
//            System.out.println("Execution time without trigger : " + execution);
//
//        }
        else{
            System.out.println("Enter the correct value!!!");
            main(new String[]{"args"});
        }
    }

    //Batch enter data
    public static void batchEnter1(int rows) throws SQLException {

        con = DriverManager.getConnection(connectionUrl);

        for (int i = 0; i < rows; i++){

            Random random = new Random();

            int userid = random.nextInt(999);
            int amt = random.nextInt(999);

            StringBuffer userBuffer = new StringBuffer();
            userBuffer.append(random.nextInt(99));

            for(int j= 0;i<6;i++){

                userBuffer.append((char) (random.nextInt(26)+'a'));
            }

            userBuffer.append(random.nextInt(9));

            String username = userBuffer.toString();

            StringBuffer nameBuffer = new StringBuffer();

            for(int j= 0;i<10;i++){

                nameBuffer.append((char) (random.nextInt(26)+'a'));
            }
            String name = nameBuffer.toString();

            String [] arr = {"!","@","#","$","%","^","&","*","("};
            StringBuffer passBuffer = new StringBuffer();

            for(int j= 0;i<10;i++){

                passBuffer.append((char) (random.nextInt(26)+'a'));
            }
            passBuffer.append(arr[random.nextInt(9)]);

            passBuffer.append(random.nextInt(9));
            passBuffer.append(random.nextInt(9));
            passBuffer.append(random.nextInt(9));

            String pass = passBuffer.toString();

            String Query = "INSERT INTO user_details VALUES (?,?,?,?,?)";

            st = con.prepareStatement(Query);

            st.setInt(1, userid);
            st.setString(2, username);
            st.setString(3, name);
            st.setString(4, pass);
            st.setDouble(5, amt);

            st.executeUpdate();
        }
    }

    //Batch enter data
    public static void batchEnter2(int rows) throws SQLException {

        con = DriverManager.getConnection(connectionUrl);

        for (int i = 0; i < rows; i++){

            Random random = new Random();

            int userid = random.nextInt(999);
            int amt = random.nextInt(999);

            StringBuffer userBuffer = new StringBuffer();
            userBuffer.append(random.nextInt(99));

            for(int j= 0;i<6;i++){

                userBuffer.append((char) (random.nextInt(26)+'a'));
            }

            userBuffer.append(random.nextInt(9));

            String username = userBuffer.toString();

            StringBuffer nameBuffer = new StringBuffer();

            for(int j= 0;i<10;i++){

                nameBuffer.append((char) (random.nextInt(26)+'a'));
            }
            String name = nameBuffer.toString();

            String [] arr = {"!","@","#","$","%","^","&","*","("};
            StringBuffer passBuffer = new StringBuffer();

            for(int j= 0;i<10;i++){

                passBuffer.append((char) (random.nextInt(26)+'a'));
            }
            passBuffer.append(arr[random.nextInt(9)]);

            passBuffer.append(random.nextInt(9));
            passBuffer.append(random.nextInt(9));
            passBuffer.append(random.nextInt(9));

            String pass = passBuffer.toString();

            String Query = "INSERT INTO user_details_new VALUES (?,?,?,?,?)";

            st = con.prepareStatement(Query);

            st.setInt(1, userid);
            st.setString(2, username);
            st.setString(3, name);
            st.setString(4, pass);
            st.setDouble(5, amt);

            st.executeUpdate();
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

        insertData(username, name, pass, repass, amt, currency);
    }

    //User Authentication
    public static void  authUser() throws Exception{
        System.out.print("Enter username : ");

        String username = sc.next() ;

        con = DriverManager.getConnection(connectionUrl);
        Statement statement = con.createStatement() ;

        String query = "SELECT name FROM user_details WHERE username = ?";

        st = con.prepareStatement(query);

        st.setString(1, username);

        ResultSet resultSetUsername = st.executeQuery();

        if ( resultSetUsername.next() ){
            int queryCount = 0 ;
            ResultSet resultSetAuthentication = null ;
            boolean passwordMatched = false ;

            do{

                System.out.print("Enter Password : " );
                String password = sc.next() ;

                query = "SELECT userid, username, wallet_amt FROM user_details WHERE username = ? AND password = ?";

                st = con.prepareStatement(query);

                st.setString(1, username);
                st.setString(2, encrypt(password));

                resultSetAuthentication = st.executeQuery();

                if( ! resultSetAuthentication.next() ){
                    System.out.print("Wrong password Enter password again \n");
                    passwordMatched = false ;
                }else{
                    passwordMatched = true ;
                }

                queryCount++ ;

            }while(queryCount < 3 && ! passwordMatched ) ;


            if( passwordMatched ){

                int userId  = resultSetAuthentication.getInt(1);
                String name = resultSetAuthentication.getString(2) ;
                double wallet_amount = resultSetAuthentication.getDouble( 3 ) ;

                System.out.println("User ID : " + userId);

                displayAll(name, wallet_amount, username);

                gameOptions(userId);

            }else {
                System.out.print("You exceeded password limit \n");

            }

        }else{

            System.out.print("No such username exists. Create an Account ");
        }
    }
    // Game Options

    static double betAmount = 0 ;

    static void gameOptions( int userId ) throws SQLException {
        int option = -1;
        double winAmt = 0;
        System.out.print("Enter 1 if you want to play Game : \n");
        System.out.print("Enter Any Key to exit : ");
        try {
            option = sc.nextInt();
        } catch (Exception e) {
            System.out.print("Thanks for visiting our casino \n");
            return;

        }
        int gameId = 0;
        if (option == 1) {
            System.out.print("Enter 1 if you want to play Roulette : \n");
            System.out.print("Enter 2 if you want to play Dice : \n");
            System.out.print("Enter Any other key to exit : ");

//            gameId = 0;
            try {
                gameId = sc.nextInt();
            } catch (Exception e) {
                System.out.print("Thanks for visiting our casino \n");
                return;
            }

            Statement statement = con.createStatement();
            int wallet_amount = 0;

            ResultSet resultSetUser = statement.executeQuery("select * from user_details where userid = " + userId);

            int minAmount = 0, maxAmount = 0;
            if (resultSetUser.next()) {
                wallet_amount = resultSetUser.getInt(5);
            }
//            resultSetUser.close();
            ResultSet resultSetGame = statement.executeQuery("select min_amt , max_amt from game_details where gid = " + gameId);
            if (resultSetGame.next()) {
                minAmount = resultSetGame.getInt(1);
                maxAmount = resultSetGame.getInt(2);

            }

            resultSetGame.close();

            do {
                System.out.print("Your wallet amount is " + wallet_amount + "\n");
                System.out.print("Enter Your bet amount in the range ( " + minAmount + " - " + maxAmount + " ) : ");
                betAmount = sc.nextInt();

            } while (betAmount <= 0 || betAmount > wallet_amount || betAmount < minAmount || betAmount > maxAmount);

            System.out.print("Select Your game condition from options given below\n");
            System.out.print("Enter 1 for odd : \n");
            System.out.print("Enter 2 for even : \n");
            System.out.print("Enter 3 for selecting your own digit : \n");
            System.out.print("Enter any other key to exit : ");

            int gameCondition = 0;

            try {
                gameCondition = sc.nextInt();

            } catch (Exception e) {
                System.out.print("Thanks for visiting our casino \n");
                return;
            }

            GameLogic game = new GameLogic(betAmount, gameCondition);

            if (gameId == 1) {

                winAmt = game.playRoulette();

            } else if (gameId == 2) {

                winAmt = game.playDice();

            }

        }
        double netamt = winAmt - betAmount;
        Statement statement = con.createStatement();
        statement.executeUpdate("UPDATE  user_details SET wallet_amt = wallet_amt + " + netamt);
        if (netamt > 0) {
            System.out.print("Congratulationss!! You Won a Profit of " + netamt + "\n");
        }

        //transaction id

        int tid = 0;
        String querytid = "SELECT TOP 1 * FROM transaction_details ORDER BY tid DESC ";
        Statement sta = con.createStatement();
        rs = sta.executeQuery( querytid );

        if (rs.next()) {
            tid = rs.getInt("tid");
        }
        tid++;



        // uid
        int uid = 0;
        String query = "SELECT userid FROM user_details ";


        Statement s = con.createStatement();
        rs = s.executeQuery(query);

        if (rs.next()) {
            uid = rs.getInt("userid");
        }
//        System.out.println(uid);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yy hh:mm:ss");

        String sql = "INSERT INTO transaction_details values(" +  tid + "," + userId + "," + gameId + "," + "convert( datetime , '" + now.format(format) + "' , 5)" + "," + betAmount + "," + winAmt +"); " ;
        Statement ps = con.createStatement();
        ps.executeUpdate(sql);

        System.out.println("Do you want to play another game?");
        System.out.print("Enter 1 if you want to see your transaction history : \n");

        System.out.println("Enter 2 you want to play or anything else to exit: ");


        try {
            int exit = sc.nextInt();
            if (exit == 1) {
                //transaction history.
//                System.out.println("enter transaction history333333");
                transactionHistory(userId);

            }
            if (exit == 2) {
                gameOptions(userId);
            }


        } catch (Exception e) {
            System.out.print("Thanks for visiting our casino \n");
            return;

        }
    }
    static void transactionHistory(int userId) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery("SELECT UID, TID, TDATETIME, bet_amount, win_amount, gname " +
                "from game_details gd inner join transaction_details td " +
                "on uid ="+userId+"  and gd.gid = td.gid" );
        System.out.println("Date/Time | Game name | Bet Amount | Win Amount");
        System.out.println("Transaction History");
        int count =1;

        while (rs.next()) {
            int tid = rs.getInt("tid");
            int uid = rs.getInt("uid");
            String date = rs.getString("TDATETIME");
            int bet_amount = rs.getInt("bet_amount");
            int win_amount = rs.getInt("win_amount");
            String gname = rs.getString("gname");
            count++;
            System.out.println(count+") "+date + "\t "+gname+"\t " + bet_amount + "\t " + win_amount);



        }
        System.out.println("Type a number to play");
        System.out.print("Anything else to exit: ");
        try {
            sc.nextInt();
            gameOptions(userId);

        }
        catch (Exception e) {
            System.out.print("Thanks for visiting our casino \n");
            return;
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
        } else if (password.length() <= 8) {
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

                con = DriverManager.getConnection(connectionUrl);

                //Auto Incrementing userid
                int userid = 0 ;
                String query = "SELECT TOP 1 userid FROM user_details ORDER BY userid DESC ";

                st = con.prepareStatement(query);
                rs = st.executeQuery();

                if(rs.next()){
                    userid = rs.getInt("userid");
                }
                userid++;

                //Inserting into table
                String Query = "INSERT INTO user_details VALUES (?,?,?,?,?)";
                st = con.prepareStatement(Query);

                st.setInt(1, userid);
                st.setString(2, username);
                st.setString(3, name);
                st.setString(4, encrypt(pass));
                st.setDouble(5, getBalance(amt, currency));

                int rowsEffected = st.executeUpdate();

                if (rowsEffected == 1) System.out.println("Succesfully Created User");
                else if(rowsEffected == 0) return false;
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
