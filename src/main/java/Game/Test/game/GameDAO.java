package Game.Test.game;

import Game.Test.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSet;

public class GameDAO extends DataAccessObject<Game> {
    private static final String GET_LIMITS = "select min_amt , max_amt from game_details where gid = ?";
    public GameDAO(Connection connection) {super(connection);}
    @Override
    public Game findById(long id) {
        return null;
    }

    public int[] getLimits(long gameId){
        int[] limits = new int[2];
        try(PreparedStatement statement = connection.prepareStatement(GET_LIMITS)){
            statement.setLong(1, gameId);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                limits[0] = rs.getInt(1);
                limits[1] = rs.getInt(2);
            }
            return limits;
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    @Override
    public ArrayList<Game> findAll() {
        return null;
    }

    @Override
    public Game create(Game dto) {
        return null;
    }

    @Override
    public Game update(Game dto) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}
