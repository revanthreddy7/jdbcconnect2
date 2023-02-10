package Game.Test.User;

import Game.Test.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends DataAccessObject<User> {

    private static final String INSERT = "INSERT INTO userdetails (username, name, password, wallet_amt) VALUES (?,?,?,?)";
    private static final String RETRIEVE_ONE = "SELECT id, username, name, password, wallet_amt FROM userdetails WHERE id = ?";
    private static final String RETRIEVE_ONE_USERNAME = "select id, username, name, password, wallet_amt FROM userdetails WHERE username = ?";
    private static final String RETRIEVE_ALL = "SELECT * FROM userdetails";
    private static final String UPDATE = "UPDATE userdetails SET username = ?, name = ?, password = ?, wallet_amt = ? WHERE id = ?";
    public UserDAO(Connection connection) {super(connection);}

    @Override
    public User findById(long id) {
        User user = new User();
        try(PreparedStatement statement = connection.prepareStatement(RETRIEVE_ONE);){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                user.setId(rs.getLong("id"));
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
    public User findByUserName(String username) {
        User user = new User();
        try(PreparedStatement statement = connection.prepareStatement(RETRIEVE_ONE_USERNAME);){
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                user.setId(rs.getLong("id"));
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
        ArrayList<User> userList = new ArrayList<User>();
        try (PreparedStatement statement = this.connection.prepareStatement(RETRIEVE_ALL);){
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setWallet_amt(rs.getDouble("wallet_amt"));
                userList.add(user);
            }
            return userList;
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(User dto) {
        try(PreparedStatement statement = this.connection.prepareStatement(INSERT);){
            long id = getLastVal(USER_TABLE);
            statement.setString(1, dto.getUsername());
            statement.setString(2, dto.getName());
            statement.setString(3, dto.getPassword());
            statement.setDouble(4, dto.getWallet_amt());
            statement.execute();
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public User update(User dto) {
        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE);) {
            statement.setString(1, dto.getUsername());
            statement.setString(2, dto.getName());
            statement.setString(3, dto.getPassword());
            statement.setDouble(4, dto.getWallet_amt());
            statement.setLong(5, dto.getId());
            statement.execute();
            return this.findById(dto.getId());
        }catch(SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    @Override
    public void delete(long id) {

    }
}
