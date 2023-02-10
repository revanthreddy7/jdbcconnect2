package Game.Test;

import Game.Test.Currency.CurrencyDAO;
import Game.Test.Transaction.*;
import Game.Test.User.*;
import Game.Test.game.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JDBCExecutor {
    static Scanner scanner;
    static Connection connection;
    static GameDAO gameDAO;
    static TransactionDAO transactionDAO;
    static CurrencyDAO currencyDAO;
    static HashMap<String, Double> currencyList;

    static Map<Integer, String> validationCodes = Map.of(1, "Username length should be greater than 3",
                                                        2, "Username already exists! Please use a different one",
                                                        3, "Please Include a minimum of 1 Digit 1 Alphabet in the Username",
                                                        4, "Passwords should have a minimum of 8 characters",
                                                        5, "Passwords do not match",
                                                        6, "Please enter the correct input",
                                                        7, "Validated");

    public static void main(String[] args) {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost", "1433", "Game", "sa", "$Fd524422");
        try{
            connection = dcm.getConnection();
            gameDAO = new GameDAO(connection);
            transactionDAO = new TransactionDAO(connection);
            currencyDAO = new CurrencyDAO(connection);
            currencyList = currencyDAO.getAll();
            UserServices userServices = new UserServices(connection);
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
                        boolean userCreated = false;
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

                            userServices.setUser(username);
                            // Creating a new user and returning the user.
                            userCreated = createUser(name, username, pass, repass, currencyChoice, amt);

                        } while(!userCreated); // This condition checks if user created successfully or not

                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                } else if (userType == 2) {
                    boolean userAuthorised = false;
                    int queryCount = 0;  // to maintain number of attempts to type passwords.

                    String username = "";
                    String password = "";
                    do {
                        // Taking user input
                        System.out.print("Enter username : ");
                        username = scanner.next();

                        System.out.print("Enter Password : ");
                        password = scanner.next();

                        userServices.setUser(username);
                        // Authorization of user.
                        userAuthorised = AuthenticationService.Authenticate(username, password);
                        queryCount++;
                        if(!userAuthorised) System.out.println("Wrong Details!");
                    } while (queryCount < 3 && !userAuthorised); // Checking if user can try again.
                    if(userAuthorised){ // verify that user is authorized

                        System.out.println("User is authorized");
                        UserServices.displayUserInfo(); // display user information
                        int exit = -1;
                        do {
                            gameOptions();
                            try {
                                exit = scanner.nextInt();
                                if (exit == 1) {
                                    //transaction history.
                                    transactionHistory(UserServices.getUserId());
                                    System.out.println("Enter 2 you want to play or anything else to exit");
                                    exit = scanner.nextInt();
                                }
                            } catch (Exception e) {
                                System.out.print("Thanks for visiting our casino \n");
                                return;
                            }
                        }while(exit == 2);
                    }else System.out.println("You have exceeded the maximum number of attempts");
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
    public static boolean createUser(String name, String username, String pass, String repass, int currencyChoice, int amt) throws Exception {
        User user = new User();
        String currency = "";
        int validationCode = AuthenticationService.Validate(username, pass, repass, currencyChoice);
        if(validationCode == 7) {
            ArrayList<String> currencies = new ArrayList<>(
                    Arrays.asList("euro", "usd", "inr", "pound", "won")
            );
            currency = currencies.get(currencyChoice - 1);
            user.setUsername(username);
            user.setName(name);
            user.setPassword(AuthenticationService.encrypt(pass));
            user.setWallet_amt(getBalance(amt, currency));
            UserServices.createUser(user);
            return true;
        }else{
            System.out.println(validationCodes.get(validationCode));
            return false;
        }
    }

    // method to get convert wallet amount to required type
    static double getBalance(double wallet_amt , String currency){
        return wallet_amt*currencyList.get(currency);
    }

    // Cant write test cases for this method, Only to take user inputs. Too lengthy to put in Main.
    static void gameOptions() {
        int option = -1;
        double winAmt = 0;
        System.out.print("Enter 1 if you want to play Game\n");
        System.out.print("Enter Any Key to exit");
        try {
            option = scanner.nextInt();
        } catch (Exception e) {
            return;
        }
        if (option == 1) {
            System.out.print("Enter 1 if you want to play Roulette \n");
            System.out.print("Enter 2 if you want to play Dice \n");
            System.out.print("Enter Any other key to exit ");
            int gameId;
            try {
                gameId = scanner.nextInt();
            } catch (Exception e) {
                return;
            }
            double wallet_amt = UserServices.getWalletAmount();
            int[] limits = gameDAO.getLimits(gameId);
            int minAmount = limits[0], maxAmount = limits[1];
            double betAmount;
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

            int gameCondition;

            try {
                gameCondition = scanner.nextInt();

            } catch (Exception e) {
                return;
            }
            playGame(UserServices.getUserId(), gameId, betAmount, gameCondition);
            System.out.println("Do you want to play another game?");
            System.out.print("Enter 1 if you want to see your transaction history\n");
            System.out.println("Enter 2 you want to play or anything else to exit");
        }
    }

    // method to play game
    static void playGame(long userId, long gameId, double betAmount, int gameCondition){

        double winAmt = 0;
        Games game = new Games(betAmount, gameCondition);
        if (gameId == 1) {
            winAmt = game.playRoulette();
        } else if (gameId == 2) {
            winAmt = game.playDice();
        }
        System.out.println("Win Amount : " + winAmt);
        // Update User Waller Amount
        if(winAmt == 0) {
            winAmt -= betAmount;
        }
        UserServices.updateWalletAmount(winAmt);


        double availableAmount = UserServices.getWalletAmount();
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
        transactionDAO.create(transaction);
    }

    // method to show transaction history
    static void transactionHistory(long userId){
        List<Transaction> transactionList = transactionDAO.betHistory(userId);
        transactionList.forEach(System.out::println);
    }
}
