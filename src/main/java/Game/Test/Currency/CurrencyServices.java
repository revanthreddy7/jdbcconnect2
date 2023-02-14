package Game.Test.Currency;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class CurrencyServices {
    private static CurrencyDAO currencyDAO;

    public CurrencyServices(Connection connection) {
        currencyDAO = new CurrencyDAO(connection);
    }

    public HashMap<String, Double> getAll() {
        ArrayList<Currency> currencies = currencyDAO.getAll();
        HashMap<String, Double> currencyList = new HashMap<>();
        for (Currency c : currencies) {
            currencyList.put(c.getCurrency(), c.getMultiplier());
        }
        return currencyList;
    }

}
