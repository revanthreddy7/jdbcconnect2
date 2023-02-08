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
                    try {
                        String createUserOutput = "";
                        do {
                            // Taking user inputs
                            int amt = 0;
                            String name = "";
                            String username = "";
                            String pass = "";
                            String repass = "";
                            int currencyChoice = 0;

                            System.out.println("Enter the name: ");
                            name = scanner.next();
                            System.out.println("Enter the username: ");
                            username = scanner.next();
                            System.out.println("Enter the Password: ");
                            pass = scanner.next();
                            System.out.println("Enter the Password again: ");
                            repass = scanner.next();
                            System.out.println("Enter the wallet Amount: ");
                            amt = scanner.nextInt();
                            System.out.println("Enter Your Choice of Currency:1:EURO, 2:USD, 3:INR, 4:POUND, 5:WON");
                            currencyChoice = scanner.nextInt();

                            // Creating a new user and returning a String to see the result.
                            createUserOutput = createUser(name, username, pass, repass, currencyChoice, amt);
                            System.out.println(createUserOutput);

                        }while(!createUserOutput.equals("User Created Successfully")); // This condition checks if user created successfully or not

                        System.out.println(createUserOutput);

                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                } else if (userType == 2) {

                    String userAuthorised = "";
                    int queryCount = 0;  // to maintain number of attempts to type passwords.

                    String username = "";
                    String password = "";
                    do {
                        // Taking user input
                        System.out.print("Enter username : ");
                        username = scanner.next();

                        System.out.print("Enter Password : ");
                        password = scanner.next();

                        // Authorization of user.
                        userAuthorised = authUser(username, password);

                        queryCount++;
                    } while (queryCount < 3 && userAuthorised.equals("Wrong password Enter password again")); // Checking that password can be entered again.

                    if(userAuthorised.equals("User Authorized Successfully")){ // verify that user is authorized

                        double[] details = userDAO.getDetails(username); // get userid and wallet amount
                        String name = userDAO.getName(username); // get name of user

                        long userId = (long) details[0];
                        double wallet_amount = details[1];

                        displayAll(name, wallet_amount, username); // display user information
                        gameOptions(userId); // play the game
                    }
                } else {
                    System.out.println("Enter the correct value!!!");
                    checkInput = true;
                }
            }while(checkInput);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Method to create and add a new user to database.
    public static String createUser(String name, String username, String pass, String repass, int currencyChoice, int amt) throws Exception {
        User user = new User();
        String currency = "";
        try{
            if(username.length()<3 ){
                return "Username length should be greater than 3";
            }else if(userDAO.getUserName(username)){
                return "Username already exists! Please use a different one";
            }
            boolean hasDigit = false;
            boolean hasAlphabet = false;

            for (int i = 0; i < username.length(); i++) {
                char f = username.charAt(i);

                if(Character.isDigit(f)) hasDigit = true;
                if(Character.isAlphabetic(f)) hasAlphabet = true;
            }
            if (!hasDigit || !hasAlphabet){
                return "Please Include a minimum of 1 Digit 1 Alphabet in the Username";
            }
            if (pass.length() <= 8) {
                return "Passwords should have a minimum of 8 characters";
            }else if (!pass.equals(repass)) {
                return "Passwords do not match";
            }
            ArrayList<String> currencies = new ArrayList<>(
                    Arrays.asList("EURO", "USD", "INR", "POUND", "WON")
            );

            currency = currencies.get(currencyChoice - 1);
            if(currencyChoice < 1 || currencyChoice >5){
                return "Please enter the correct input!!";
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        user.setUsername(username);
        user.setName(name);
        user.setPassword(encrypt(pass));
        user.setWallet_amt(getBalance(amt, currency));
        user = userDAO.create(user);
        return "User Created Successfully";
    }

    // method to authorize the user.
    public static String authUser(String username, String password){
        try {
            String name = userDAO.getName(username);
            if (name.compareTo("") != 0) {
                boolean passwordMatched = false;
                if (!userDAO.checkUser(username, encrypt(password))) {
                    return "Wrong password Enter password again";
                }
                return "User Authorized Successfully";
            } else {
                return "No such username exists. Create an Account ";
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // method to get convert wallet amount to required type
    static double getBalance(double wallet_amt , String currency){

        if( currency.equalsIgnoreCase("USD") )return wallet_amt * 0.92 ;
        else if( currency.equalsIgnoreCase("INR") )return wallet_amt * 0.011 ;
        else if( currency.equalsIgnoreCase("POUND") )return wallet_amt * 0.0071 ;
        else if( currency.equalsIgnoreCase("WON") )return wallet_amt * 0.00075 ;

        return wallet_amt ;
    }


    // method to play game
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

    // method to show transaction history
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
    // method to display user information
    public static void displayAll(String name, double wallet_amount, String username){
        System.out.println("***-------****-------***");
        System.out.print("Name          : " + name + "\n" ) ;
        System.out.print("Username      : " + username + "\n" ) ;
        System.out.print("Wallet Amount : " + wallet_amount + "\n" ) ;
        System.out.println("***-------****-------***");
    }
}
