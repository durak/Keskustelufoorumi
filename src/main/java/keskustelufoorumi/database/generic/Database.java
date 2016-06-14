package keskustelufoorumi.database.generic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    
    private final String databaseAddress;
    private Connection connection;
    
    public Database() throws Exception {
        this.databaseAddress = "jdbc:sqlite:forum.db";
        
    }
    
    public void open() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            this.connection = DriverManager.getConnection(databaseAddress);
        }
    }
    
    public void close() throws SQLException {
        if (this.connection != null || !this.connection.isClosed()) {
            this.connection.close();
        }
    }
}
