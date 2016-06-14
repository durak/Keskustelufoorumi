package keskustelufoorumi.database.generic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import keskustelufoorumi.domain.Kayttaja;

public class KayttajaDao extends GenericDao<Kayttaja> {

    private final static String TABLENAME = "Kayttaja";

    public KayttajaDao(Connection connection) {
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
}
