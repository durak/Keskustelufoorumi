package keskustelufoorumi.database.sql;

import keskustelufoorumi.domain.Kayttaja;
import java.sql.*;
import java.util.*;
import keskustelufoorumi.database.Database;
import keskustelufoorumi.database.KayttajaDao;

public class SqlKayttajaDao implements KayttajaDao {

    private Database database;

    public SqlKayttajaDao(Database database) {
        this.database = database;

    }

    @Override
    public Kayttaja findOne(String id) throws SQLException {
        Connection connection = database.getConnection();

        String query = "SELECT * FROM Kayttaja WHERE id = ?;";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, id);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        id = rs.getString("id");
        Kayttaja kayttaja = new Kayttaja(id);

        rs.close();
        stmt.close();

        connection.close();

        return kayttaja;
    }

    @Override
    public void insertNewKayttaja(Kayttaja kayttaja) throws SQLException {
        String updateQuery = "INSERT INTO Kayttaja VALUES (?);";
        String[] params = {kayttaja.getId()};
        update(updateQuery, params);
    }

    /*
     * Apumetodit
     * findAllWithQueryAndParams: lukukyselyt
     * update: kirjoittavat kyselyt
     */
    private void update(String updateQuery, Object... params) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(updateQuery);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

}
