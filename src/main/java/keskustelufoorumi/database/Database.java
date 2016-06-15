package keskustelufoorumi.database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private String databaseAddress = "jdbc:sqlite:forum.db";
    private static Database instance = new Database();
    private Connection connection;

    private Database() {
    }

    public static Database getInstance() {
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
