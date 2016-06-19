
package keskustelufoorumi.database.genericEI_KAYTOSSA;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class GenericDaoBS<T> {
    
    public abstract int count() throws SQLException;
    
    protected Connection connection;
    protected final String tableName;
    
    
    protected GenericDaoBS(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
        
    }
}
