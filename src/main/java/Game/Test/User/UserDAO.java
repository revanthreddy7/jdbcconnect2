package Game.Test.User;

import Game.Test.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO extends DataAccessObject<User> {

    private static final String INSERT = "INSERT INTO userdetails (userid, username, name, password, wallet_amt) VALUES (?,?,?,?,?)";
    private static final String GET_ONE = "SELECT userid, username, name, password, wallet_amt FROM userdetails WHERE userid = ?";
    private static final String GET_NAME = "SELECT name FROM userdetails WHERE username = ?";
    private static final String CHECK_USER = "SELECT COUNT(*) FROM userdetails WHERE username= ? AND password= ?";
    private static final String GET_DETAILS = "SELECT userid, wallet_amt FROM userdetails WHERE username = ?";
    private static final String GET_WALLET_AMT = "select wallet_amt from userdetails where userid = ?";
    private static final String UPDATE_WALLET = "UPDATE  userdetails SET wallet_amt = wallet_amt + ?";
    public UserDAO(Connection connection) {super(connection);}

    @Override
    public User findById(long id) {
        User user = new User();
        try(PreparedStatement statement = connection.prepareStatement(GET_ONE);){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                user.setId(rs.getLong("userid"));
                user.setUsername(rs.getString("username"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setWallet_amt(rs.getDouble("wallet_amt"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public ArrayList<User> findAll() {
        return null;
    }

    @Override
    public User create(User dto) {
        try(PreparedStatement statement = this.connection.prepareStatement(INSERT);){
            long id = getLastVal(USER_SEQUENCE);
            statement.setLong(1, id);
            statement.setString(2, dto.getUsername());
            statement.setString(3, dto.getName());
            statement.setString(4, dto.getPassword());
            statement.setDouble(5, dto.getWallet_amt());
            statement.execute();
            return this.findById(id);
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void updateWallet(double winAmount){
        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE_WALLET);){
            statement.setDouble(1, winAmount);
            statement.execute();
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public String getName(String username) {
        String name = "";
        try(PreparedStatement statement = this.connection.prepareStatement(GET_NAME);){
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                name = rs.getString("name");
            }
            return name;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public double getWalletAmt(Long id){
        double walletAmt = 0;
        try(PreparedStatement statement = this.connection.prepareStatement(GET_WALLET_AMT);){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                walletAmt = rs.getDouble("wallet_amt");
            }
            return walletAmt;
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public boolean checkUser(String username, String password) {
        String details = "";
        try(PreparedStatement statement = this.connection.prepareStatement(CHECK_USER);) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                return true;
            }
            return false;
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public double[] getDetails(String username) {
        double[] details = new double[2];
        try(PreparedStatement statement = this.connection.prepareStatement(GET_DETAILS);) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                details[0] = ((rs.getLong("userid")*1.0));
                details[1] = (rs.getDouble("wallet_amt"));
            }
            return details;
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    @Override
    public User update(User dto) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}
