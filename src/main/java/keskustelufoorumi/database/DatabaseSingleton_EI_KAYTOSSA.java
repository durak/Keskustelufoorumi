package keskustelufoorumi.database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseSingleton_EI_KAYTOSSA {

    private String databaseAddress = "jdbc:sqlite:forum.db";
    private static DatabaseSingleton_EI_KAYTOSSA instance = new DatabaseSingleton_EI_KAYTOSSA();
    private Connection connection;

    private DatabaseSingleton_EI_KAYTOSSA() {
    }

    public static DatabaseSingleton_EI_KAYTOSSA getInstance() {
        return instance;
    }

//    public Database(String databaseAddress) throws ClassNotFoundException {
//        this.databaseAddress = databaseAddress;
//    }
    public Connection getConnection() throws SQLException {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(databaseAddress);
            } catch (SQLException ex) {
                Logger.getLogger(DaoManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return this.connection;
    }
    
    private Connection getTxConnection() {
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(DaoManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return this.connection;
    }
}
