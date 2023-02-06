package Game.Test.Transaction;

import Game.Test.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;

public class TransactionDAO extends DataAccessObject<Transaction> {
    private static final String INSERT = "INSERT INTO transaction_details VALUES (tid, uid, gid, tdatetime, bet_amount, win_amount) VALUES (?,?,?,?,?,?)";
    private static final String HISTORY = "SELECT tid, uid, gid, tdatetime, bet_amount, win_amount FROM transaction_details WHERE userid = ?";

    public TransactionDAO(Connection connection){super(connection);}
    @Override
    public Transaction findById(long id) {
        return null;
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
            if(rs.next()) {
                Long id = rs.getLong("tid");
                Long gameId = rs.getLong("gid");
                Date datetime = rs.getDate("tdatetime");
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
            statement.setDate(4, dto.getDateTime());
            statement.setDouble(5, dto.getBetAmount());
            statement.setDouble(5, dto.getWinAmount());
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
