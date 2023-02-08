package Game.Test;

import Game.Test.Transaction.*;
import Game.Test.User.*;
import Game.Test.game.*;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JDBCExecutor {

    private static final String ALGORITHM = "AES";
    private static final byte[] KEY = "MySuperSecretKey".getBytes(StandardCharsets.UTF_8);
    static Scanner scanner;
    static Connection connection;
    static UserDAO userDAO;
    static GameDAO gameDAO;
    static TransactionDAO transactionDAO;
    public static void main(String[] args) {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost", "1433", "Game", "sa", "$Fd524422");
        try{
            connection = dcm.getConnection();

            userDAO = new UserDAO(connection);
            gameDAO = new GameDAO(connection);
            transactionDAO = new TransactionDAO(connection);
            scanner = new Scanner(System.in);

            // Taking input for the type of User
            System.out.println("Enter 1 if you are a new user");
            System.out.println("Enter 2 if you are existing user");
            User user;
            boolean checkInput = false;
            do {
                int userType = scanner.nextInt();
                if (userType == 1) {
                    int amt = 0;
                    int choice = 0;
                    String name = "";
                    String username = "";
                    String pass = "";
                    String repass = "";
                    String currency = "";

                    try{
                        boolean check = false;
                        do {
                            System.out.println("Enter the name: ");
                            name = scanner.next();

                            System.out.println("Enter the username: ");
                            username = scanner.next();
                            if(username.length()<3 ){
                                System.out.println("Username length should be greater than 3");
                                check = true;
                                continue;
                            }else if(userDAO.getUserName(username)){
                                System.out.println("Username already exists! Please use a different one");
                                check = true;
                                continue;
                            }
                            boolean hasDigit = false;
                            boolean hasAlphabet = false;

                            for (int i = 0; i < username.length(); i++) {
                                char f = username.charAt(i);

                                if(Character.isDigit(f)) hasDigit = true;
                                if(Character.isAlphabetic(f)) hasAlphabet = true;
                            }
                            if (!hasDigit || !hasAlphabet){
                                System.out.println("Please Include a minimum of 1 Digit 1 Alphabet in the Username");
                                check = true;
                                continue;
                            }
                            System.out.println("Enter the Password: ");
                            pass = scanner.next();

                            System.out.println("Enter the Password again: ");
                            repass = scanner.next();

                            if (pass.length() <= 8) {
                                System.out.println("Passwords should have a minimum of 8 characters");
                                check = true;
                                continue;
                            }else if (!pass.equals(repass)) {
                                System.out.println("Passwords do not match");
                                check = true;
                                continue;
                            }

                            System.out.println("Enter the wallet Amount: ");
                            amt = scanner.nextInt();

                            System.out.println("Enter Your Choice of Currency:1:EURO, 2:USD, 3:INR, 4:POUND, 5:WON");
                            choice = scanner.nextInt();
                            ArrayList<String> currencies = new ArrayList<>(
                                    Arrays.asList("EURO", "USD", "INR", "POUND", "WON")
                            );

                            currency = currencies.get(choice - 1);
                            if(choice < 1 || choice >5){
                                System.out.println("Please enter the correct input!!");
                                check = true;
                                continue;
                            }
                            setUpUser(username, name, pass, amt, currency);

                            check = false;
                        }while(check);
                    }
                    catch (Exception e){
                        System.err.println(e);
//                        setUpUser(user);
                    }




                }

                else if (userType == 2) {
                    System.out.print("Enter username : ");
                    String usernameLogin = scanner.next();
                    System.out.print("Enter Password : ");

                    String passwordLogin = scanner.next();
                    authUser(usernameLogin, passwordLogin);
                } else {
                    System.out.println("Enter the correct value!!!");
                    checkInput = true;
                }
            }while(checkInput);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void setUpUser( String username, String name, String pass, int amt, String currency ) throws Exception {

        User user = new User();


        user.setUsername(username);
        user.setName(name);
        user.setPassword(encrypt(pass));
        user.setWallet_amt(getBalance(amt, currency));
        user = userDAO.create(user);
    }

    public static void authUser(String username, String password) {
        try {


            String name = userDAO.getName(username);

            if (name.compareTo("") != 0) {
                int queryCount = 0;
                boolean passwordMatched = false;
                do {
                    if (!userDAO.checkUser(username, encrypt(password))) {
                        System.out.print("Wrong password Enter password again \n");
                    } else {
                        passwordMatched = true;
                    }
                    queryCount++;
                } while (queryCount < 3 && !passwordMatched);


                if (passwordMatched) {
                    double[] details = userDAO.getDetails(username);

                    long userId = (long) details[0];
                    double wallet_amount = details[1];

                    displayAll(name, wallet_amount, username);

                    gameOptions(userId);
                } else {
                    System.out.print("You exceeded password limit \n");
                }
            } else {
                System.out.print("No such username exists. Create an Account ");
            }
        }
        catch (Exception e){
            System.err.println(e);
        }
    }
    static double getBalance(double wallet_amt , String currency){

        if( currency.equalsIgnoreCase("USD") )return wallet_amt * 0.92 ;
        else if( currency.equalsIgnoreCase("INR") )return wallet_amt * 0.011 ;
        else if( currency.equalsIgnoreCase("POUND") )return wallet_amt * 0.0071 ;
        else if( currency.equalsIgnoreCase("WON") )return wallet_amt * 0.00075 ;

        return wallet_amt ;
    }

    static void gameOptions(long userId){
        int option = -1 ;
        double winAmt = 0;
        System.out.print("Enter 1 if you want to play Game\n");
        System.out.print("Enter Any Key to exit");
        try {
            option = scanner.nextInt() ;
        }catch ( Exception e ){
            System.out.print("Thanks for visiting our casino \n");
            return ;

        }
        if( option == 1 ) {
            System.out.print("Enter 1 if you want to play Roulette \n");
            System.out.print("Enter 2 if you want to play Dice \n");
            System.out.print("Enter Any other key to exit ");

            int gameId = 0;
            try {
                gameId = scanner.nextInt();
            } catch (Exception e) {
                System.out.print("Thanks for visiting our casino \n");
                return;
            }

            double wallet_amt = userDAO.getWalletAmt(userId);
            int[] limits = gameDAO.getLimits(gameId);
            int minAmount = limits[0], maxAmount = limits[1];
            double betAmount = 0;
            do {
                System.out.print("Your wallet amount is " + wallet_amt + "\n");
                System.out.print("Enter Your bet amount in the range ( " + minAmount + " - " + maxAmount + " ) : ");
                betAmount = scanner.nextInt();

            } while (betAmount <= 0 || betAmount > wallet_amt || betAmount < minAmount || betAmount > maxAmount);

            System.out.print("Select Your game condition from options given below\n");
            System.out.print("Enter 1 for odd\n");
            System.out.print("Enter 2 for even\n");
            System.out.print("Enter 3 for selecting your own digit\n");
            System.out.print("Enter any other key to exit");

            int gameCondition = 0;

            try {
                gameCondition = scanner.nextInt();

            } catch (Exception e) {
                System.out.print("Thanks for visiting our casino \n");
                return;
            }

            Games game = new Games(betAmount, gameCondition);

            if (gameId == 1) {

                winAmt = game.playRoulette();

            } else if (gameId == 2) {

                winAmt = game.playDice();

            }
            // Update User Waller Amount
            userDAO.updateWallet(winAmt);
            System.out.println(winAmt);

            double availableAmount = userDAO.getWalletAmt(userId);
            System.out.print("Available Amount : " + availableAmount + "\n");

            // Add Transaction to Table
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yy hh:mm:ss");

            Transaction transaction = new Transaction();
            transaction.setUserId(userId);
            transaction.setGameId(gameId);
            transaction.setDateTime(now.format(format));
            transaction.setBetAmount(betAmount);
            transaction.setWinAmount(winAmt);
            transaction = transactionDAO.create(transaction);

            System.out.println("Do you want to play another game?");

            System.out.print("Enter 1 if you want to see your transaction history\n");

            System.out.println("Enter 2 you want to play or anything else to exit");


            try {
                int exit = scanner.nextInt();
                if (exit == 1) {
                    //transaction history.
                    transactionHistory(userId);

                }
                if (exit == 2) {
                    gameOptions(userId);
                }


            } catch (Exception e) {
                System.out.print("Thanks for visiting our casino \n");
            }
        }
    }
    static void transactionHistory(long userId){
        List<Transaction> transactionList = transactionDAO.betHistory(userId);
        transactionList.forEach(System.out::println);
        System.out.println("Type a number to play");
        System.out.print("Anything else to exit: ");
        try {
            scanner.nextInt();
            gameOptions(userId);
        }
        catch (Exception e) {
            System.out.print("Thanks for visiting our casino \n");
        }
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
    public static void displayAll(String name, double wallet_amount, String username){
        System.out.println("***-------****-------***");
        System.out.print("Name          : " + name + "\n" ) ;
        System.out.print("Username      : " + username + "\n" ) ;
        System.out.print("Wallet Amount : " + wallet_amount + "\n" ) ;
        System.out.println("***-------****-------***");
    }
}
