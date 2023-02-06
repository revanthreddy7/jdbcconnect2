package Game.Test.Transaction;

import Game.Test.User.User;
import Game.Test.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class TransactionDAO extends DataAccessObject<Transaction> {
    private static final String INSERT = "INSERT INTO transaction_details (tid, uid, gid, tdatetime, bet_amount, win_amount) VALUES (?,?,?,convert(datetime, ?, 5),?,?)";
    private static final String HISTORY = "SELECT tid, uid, gid, tdatetime, bet_amount, win_amount FROM transaction_details WHERE uid = ?";
    private static final String GET_ONE_T = "SELECT tid, uid, gid, tdatetime, bet_amount, win_amount FROM transaction_details WHERE tid = ?";
    public TransactionDAO(Connection connection){super(connection);}
    @Override
    public Transaction findById(long id) {
        Transaction transaction = new Transaction();
        try(PreparedStatement statement = connection.prepareStatement(GET_ONE_T);){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                transaction.setId(rs.getLong("tid"));
                transaction.setUserId(rs.getLong("uid"));
                transaction.setGameId(rs.getLong("gid"));
                transaction.setDateTime(rs.getString("tdatetime"));
                transaction.setBetAmount(rs.getDouble("bet_amount"));
                transaction.setWinAmount(rs.getDouble("win_amount"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return transaction;
    }

    @Override
    public ArrayList<Transaction> findAll() {
        return null;
    }

    public ArrayList<Transaction> betHistory(long userId) {
        try(PreparedStatement statement = this.connection.prepareStatement(HISTORY);){
            ArrayList <Transaction> bets = new ArrayList<>();
            statement.setLong(1, userId);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                long id = rs.getLong("tid");
                long gameId = rs.getLong("gid");
                String datetime = rs.getString("tdatetime");
                double betAmount = rs.getDouble("bet_amount");
                double winAmount = rs.getDouble("bet_amount");

                Transaction transaction = new Transaction();

                transaction.setId(id);
                transaction.setUserId(userId);
                transaction.setGameId(gameId);
                transaction.setDateTime(datetime);
                transaction.setBetAmount(betAmount);
                transaction.setWinAmount(winAmount);
                bets.add(transaction);
            }
            return bets;
        }
        catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    @Override
    public Transaction create(Transaction dto) {
        try(PreparedStatement statement = this.connection.prepareStatement(INSERT);){
            long id = getLastVal(TRANSACTION_SEQUENCE);
            statement.setLong(1, id);
            statement.setLong(2, dto.getUserId());
            statement.setLong(3, dto.getGameId());
            statement.setString(4, dto.getDateTime());
            statement.setDouble(5, dto.getBetAmount());
            statement.setDouble(6, dto.getWinAmount());
            statement.execute();
            return this.findById(id);
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction update(Transaction dto) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}
