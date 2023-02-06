package Game.Test;

import Game.Test.User.User;
import Game.Test.User.UserDAO;
import Game.Test.game.GameDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class JDBCExecutor {

    static Scanner scanner;
    static Connection connection;
    static UserDAO userDAO;
    static GameDAO gameDAO;
    public static void main(String[] args) {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost", "1433", "Game", "sa", "$Fd524422");
        try{
            connection = dcm.getConnection();

            userDAO = new UserDAO(connection);
            gameDAO = new GameDAO(connection);
            scanner = new Scanner(System.in);

            // Taking input for the type of User
            System.out.println("Enter 1 if you are a new user");
            System.out.println("Enter 2 if you are existing user");
            System.out.println("Enter 3 for testing");

            User user;

            int userType = scanner.nextInt();
            if (userType==1){
                try {
                    user = new User();
                    setUpUser(user);
                    user = userDAO.create(user);
                }
                catch (Exception e){
                    System.err.println(e);
                }
            } else if (userType==2) {
                try {
                    System.out.print("Enter username : ");
                    String username = scanner.next();
                    String name = userDAO.getName(username);

                    if (name.compareTo("") != 0) {
                        int queryCount = 0;
                        boolean passwordMatched = false;

                        do {
                            System.out.print("Enter Password : ");
                            String password = scanner.next();
                            if (!userDAO.checkUser(username, password)) {
                                System.out.print("Wrong password Enter password again \n");
                                passwordMatched = false;
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
            else if (userType==3){
//                System.out.println("Enter Number of rows to be tested: ");
//                Scanner sc = new Scanner(System.in);
//                int rows = sc.nextInt();
//
//                long start;
//                long end;
//                long execution;
//
//                // get the start time
//                start = System.currentTimeMillis();
//
//                // call the method
//                batchEnter1(rows);
//
//                // get the end time
//                end = System.currentTimeMillis();
//
//                // execution time
//                execution = end - start;
//
//                System.out.println("Execution time with trigger : " + execution);
//
//                // get the start time
//                start = System.currentTimeMillis();
//
//                // call the method
//                batchEnter2(rows);
//
//                // get the end time
//                end = System.currentTimeMillis();
//
//                // execution time
//                execution = end - start;

//                System.out.println("Execution time without trigger : " + execution);

            }
            else{
                System.out.println("Enter the correct value!!!");
                main(new String[]{"args"});
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void setUpUser(User user){

        int amt = 0;
        int choice = 0;
        String name = "";
        String username = "";
        String pass = "";
        String repass = "";
        String currency = "";

        try{

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
            choice = scanner.nextInt();
            ArrayList<String> currencies = new ArrayList<>(
                    Arrays.asList("EURO", "USD", "INR", "POUND", "WON")
            );

            currency = currencies.get(choice);

            if(choice<1 || choice>5){
                System.out.println("Please enter the correct input!!");
                setUpUser(user);
            }
        }
        catch (Exception e){
            System.err.println(e);
            setUpUser(user);
        }
        user.setUsername(username);
        user.setName(name);
        user.setPassword(pass);
        user.setWallet_amt(getBalance(amt, currency));
    }

    static double getBalance(double wallet_amt , String currency){

        if( currency.equalsIgnoreCase("USD") )return wallet_amt * 0.92 ;
        else if( currency.equalsIgnoreCase("INR") )return wallet_amt * 0.011 ;
        else if( currency.equalsIgnoreCase("POUND") )return wallet_amt * 0.0071 ;
        else if( currency.equalsIgnoreCase("WON") )return wallet_amt * 0.00075 ;

        return wallet_amt ;
    }

    static void gameOptions( long userId ){
        int option = -1 ;
        double winAmt = 0;
        System.out.print("Enter 1 if you want to play Game : \n");
        System.out.print("Enter Any Key to exit : ");
        try {
            option = scanner.nextInt() ;
        }catch ( Exception e ){
            System.out.print("Thanks for visiting our casino \n");
            return ;

        }
        if( option == 1 ) {
            System.out.print("Enter 1 if you want to play Roulette : \n");
            System.out.print("Enter 2 if you want to play Dice : \n");
            System.out.print("Enter Any other key to exit : ");

            int gameId = 0;
            try {
                gameId = scanner.nextInt();
            } catch (Exception e) {
                System.out.print("Thanks for visiting our casino \n");
                return;
            }

            double wallet_amt = userDAO.getWalletAmt(userId);
            int[] limits = gameDAO.getLimits(gameId);
            int minAmount =  limits[0], maxAmount = limits[1];
            double betAmount = 0 ;
            do{
                System.out.print("Your wallet amount is " + wallet_amt+ "\n" );
                System.out.print("Enter Your bet amount in the range ( " + minAmount + " - " + maxAmount + " ) : " );
                betAmount = scanner.nextInt() ;

            }while(  betAmount <= 0 || betAmount > wallet_amt  || betAmount < minAmount || betAmount > maxAmount ) ;

            System.out.print("Select Your game condition from options given below\n");
            System.out.print("Enter 1 for odd\n");
            System.out.print("Enter 2 for even\n");
            System.out.print("Enter 3 for selecting your own digit\n");
            System.out.print("Enter any other key to exit");

            int gameCondition = 0 ;

            try {
                gameCondition = scanner.nextInt() ;

            } catch (Exception e) {
                System.out.print("Thanks for visiting our casino \n");
                return;
            }

            Games game = new Games(betAmount, gameCondition);

            if ( gameId == 1) {

                winAmt = game.playRoulette();

            } else if( gameId == 2 ){

                winAmt = game.playDice();

            }

        }
//        statement.executeUpdate("UPDATE  userdetails SET wallet_amt = wallet_amt + " + winAmt);
        userDAO.updateWallet(userId);
        System.out.println(winAmt);

        double availableAmount = userDAO.getWalletAmt(userId);
        System.out.print("Available Amount : " + availableAmount + "\n" );
    }
    public static void displayAll(String name, double wallet_amount, String username){
        System.out.println("***-------****-------***");
        System.out.print("Name          : " + name + "\n" ) ;
        System.out.print("Username      : " + username + "\n" ) ;
        System.out.print("Wallet Amount : " + wallet_amount + "\n" ) ;
        System.out.println("***-------****-------***");
    }
}
