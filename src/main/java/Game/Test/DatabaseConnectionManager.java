package Game.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionManager {
    private final String url;
    public DatabaseConnectionManager(String host, String port, String databaseName, String username, String password) {
        this.url = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + databaseName + ";user=" + username + ";password=" + password +";encrypt=true;trustServerCertificate=true; ";
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.url);
    }
}
