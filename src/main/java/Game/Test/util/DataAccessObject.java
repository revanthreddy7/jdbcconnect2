package Game.Test.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public abstract class DataAccessObject <T extends DataTransferObject> {

    protected final Connection connection;
    protected final static String LAST_VAL = "SELECT TOP 1 id FROM ";
    protected final static String USER_TABLE = "userdetails ORDER BY id DESC";
    protected final static String TRANSACTION_TABLE = "transaction_details ORDER BY id DESC";

    public DataAccessObject(Connection connection) {
        super();
        this.connection = connection;
    }

    public abstract T findById(long id);
    public abstract ArrayList<T> findAll();
    public abstract void create(T dto);
    public abstract T update(T dto);
    public abstract void delete(long id);

    protected int getLastVal(String table){
        int key = 0;
        String sql = LAST_VAL + table;
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                key = rs.getInt(1);
            }
            return key;
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
