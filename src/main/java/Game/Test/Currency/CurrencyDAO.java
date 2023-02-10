package Game.Test.Currency;

import Game.Test.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CurrencyDAO extends DataAccessObject<Currency> {

    private static final String RETRIEVE = "SELECT * FROM currencyValue";

    public CurrencyDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Currency findById(long id) {
        return null;
    }

    @Override
    public ArrayList<Currency> findAll() {
        return null;
    }

    public HashMap<String, Double> getAll() {
        HashMap<String, Double> currencyList = new HashMap<>();
        try(PreparedStatement statement = connection.prepareStatement(RETRIEVE);){
            ResultSet resultSet = statement.executeQuery() ;
            while (resultSet.next() ){
                long id = resultSet.getLong(1);
                String currency = resultSet.getString(2);
                double multiplier = resultSet.getDouble(3);
                currencyList.put(currency, multiplier);
            }
            return currencyList;
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Currency dto) {

    }

    @Override
    public Currency update(Currency dto) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}
