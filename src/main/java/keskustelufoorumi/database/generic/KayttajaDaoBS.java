package keskustelufoorumi.database.generic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import keskustelufoorumi.domain.Kayttaja;

public class KayttajaDaoBS extends GenericDaoBS<Kayttaja> {

    private final static String TABLENAME = "Kayttaja";

    public KayttajaDaoBS(Connection connection) {
        super(connection, TABLENAME);
    }

    @Override
    public int count() throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM " + this.tableName;
        PreparedStatement counter;

        try {
            counter = this.connection.prepareStatement(query);
            ResultSet rs = counter.executeQuery();
            rs.next();
            return rs.getInt("count");
        } catch (SQLException e) {
            throw e;
        }

    }
    
        
    public Kayttaja findOne(String key) throws SQLException {        

        String query = "SELECT * FROM Kayttaja WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        String id = rs.getString("id");
        Kayttaja k = new Kayttaja(id);

        rs.close();
        stmt.close();
        connection.close();

        return k;
    }
}
