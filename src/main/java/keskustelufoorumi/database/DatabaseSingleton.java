package keskustelufoorumi.database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseSingleton {

    private String databaseAddress = "jdbc:sqlite:forum.db";
    private static DatabaseSingleton instance = new DatabaseSingleton();
    private Connection connection;

    private DatabaseSingleton() {
    }

    public static DatabaseSingleton getInstance() {
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
