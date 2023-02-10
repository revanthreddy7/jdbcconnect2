package Game.Test.User;

import java.sql.Connection;

public class UserServices {
    private static User user;
    private static UserDAO userDAO;
    public UserServices(Connection connection){
        userDAO = new UserDAO(connection);
    }
    public void setUser(String username){
        user = userDAO.findByUserName(username);
    }
    public static void createUser(User user) {
        userDAO.create(user);
    }
    public static boolean checkUser(String username, String password) {
        if(user.getUsername()!=null) {
            return user.getUsername().equals(username) && user.getPassword().equals(password);
        }
        return false;
    }
    public static long getUserId() {
        return user.getId();
    }

    public static double getWalletAmount() {
        return user.getWallet_amt();
    }
    public static void updateWalletAmount(double amount){
        user.setWallet_amt(user.getWallet_amt() + amount);
        userDAO.update(user);
    }
    public static String getName() {
        return user.getName();
    }

    public static void displayUserInfo(){
        System.out.println("***-------****-------***");
        System.out.print("Name          : " + user.getName() + "\n" ) ;
        System.out.print("Username      : " + user.getUsername() + "\n" ) ;
        System.out.print("Wallet Amount : " + user.getWallet_amt() + "\n" ) ;
        System.out.println("***-------****-------***");
    }
}
