package keskustelufoorumi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private String databaseAddress;

    public Database() throws ClassNotFoundException {
        this.databaseAddress = "jdbc:sqlite:forum.db";
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }
}