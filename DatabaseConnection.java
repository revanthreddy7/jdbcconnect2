import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
    private final String url;

    public DatabaseConnection (String databaseName, String username, String password) {
        this.url = "jdbc:sqlserver://localhost:1433;" + "databaseName=" + databaseName + ";" + "user=" + username + ";"  + "password=" + password + ";" + "encrypt=true;trustServerCertificate=true";
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.url);
    }
}
