import java.sql.*;

class UserDAO{
    static ResultSet rs;
    static Statement st;
    static Connection con;
    static PreparedStatement pst;
    static DatabaseConnection dbConnection;
    
    private static String username;
    private static String database;
    private static String password;
    private static final String getLastRecord = "SELECT TOP 1 * FROM userdetails ";
    private static final String insert = "INSERT INTO userdetails VALUES (?,?,?,?,?)";
    private static final String getUserDetails = "SELECT * FROM userdetails WHERE username = ?";
    private static final String autheticateUser = "SELECT * FROM userdetails WHERE username = ? && password = ?";

    public UserDAO() throws SQLException{
        dbConnection = new DatabaseConnection(database, username, password);
        con = dbConnection.getConnection();
    }

    public int insertData(User user) throws SQLException{
        pst = con.prepareStatement(insert);
        pst.setInt(1, user.getUserid());
        pst.setString(2, user.getUsername());
        pst.setString(3, user.getName());
        pst.setString(4, user.getPassword());
        pst.setDouble(5, user.getWallet_amt());
        return pst.executeUpdate();
    }

    public User getData(String username) throws SQLException{
        pst = con.prepareStatement(getUserDetails);
        pst.setString(1, username);
        rs = pst.executeQuery();
        rs.next();
        User user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5));
        rs.close();
        return user;
    }

    public int getUserid() throws SQLException{
        int userId = 0;
        st = con.createStatement();
        rs = st.executeQuery(getUserDetails);
        if(rs.next()) userId = rs.getInt(1);
        return userId++;
    }

    public User getLatestUser() throws SQLException{
        st = con.createStatement();
        rs = st.executeQuery(getLastRecord);
        rs.next();
        User user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5));
        rs.close();
        return user;
    }
    public Boolean checkUserExists() throws SQLException{
        pst = con.prepareStatement(getUserDetails);
        pst.setString(1, username);
        rs = pst.executeQuery();
        if(rs.next()) return true;
        return false;
    }
    public Boolean authUser() throws SQLException{
        pst = con.prepareStatement(autheticateUser);
        pst.setString(1, username);
        pst.setString(2, password);
        rs = pst.executeQuery();
        if(rs.next()) return true;
        return false;
    }
}
