package Game.Test.Currency;


import Game.Test.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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


    public ArrayList<Currency> getAll(){
        ArrayList<Currency> currencies = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(RETRIEVE)){
            ResultSet resultSet = statement.executeQuery() ;
            while (resultSet.next() ){
                Currency currency  = new Currency();
                currency.setId(resultSet.getLong(1));
                currency.setCurrency(resultSet.getString(2));
                currency.setMultiplier(resultSet.getDouble(3));
                currencies.add(currency);
            }
            return currencies;
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
