package Game.Test.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public abstract class DataAccessObject <T extends DataTransferObject> {

    protected final Connection connection;
    protected final static String LAST_VAL = "SELECT NEXT VALUE FOR ";
    protected final static String USER_SEQUENCE = "user_sequence";
    protected final static String TRANSACTION_SEQUENCE = "transaction_sequence";

    public DataAccessObject(Connection connection) {
        super();
        this.connection = connection;
    }

    public abstract T findById(long id);
    public abstract ArrayList<T> findAll();
    public abstract T create(T dto);
    public abstract T update(T dto);
    public abstract void delete(long id);

    protected int getLastVal(String sequence){
        int key = 0;
        String sql = LAST_VAL + sequence;
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
