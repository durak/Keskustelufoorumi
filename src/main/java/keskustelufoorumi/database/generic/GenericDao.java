
package keskustelufoorumi.database.generic;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class GenericDao<T> {
    
    public abstract int count() throws SQLException;
    
    protected Connection connection;
    protected final String tableName;
    
    
    protected GenericDao(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
        
    }
}
