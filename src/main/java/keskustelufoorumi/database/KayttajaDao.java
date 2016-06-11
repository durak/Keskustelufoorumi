package keskustelufoorumi.database;

import keskustelufoorumi.domain.Kayttaja;
import java.sql.*;
import java.util.*;

public class KayttajaDao implements Dao<Kayttaja, String> {

    private Database database;

    public KayttajaDao(Database database) {
        this.database = database;
    }

    @Override
    public Kayttaja findOne(String key) throws SQLException {
        Connection connection = database.getConnection();

        String query = "SELECT * FROM Kayttaja WHERE kayttajatunnus = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        String kayttajatunnus = rs.getString("kayttajatunnus");
        Kayttaja k = new Kayttaja(kayttajatunnus);

        rs.close();
        stmt.close();
        connection.close();

        return k;
    }

    @Override
    public List<Kayttaja> findAll() throws SQLException {
        Connection connection = database.getConnection();

        String query = "SELECT * FROM Kayttaja";
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        List<Kayttaja> kayttajat = new ArrayList<>();

        while (rs.next()) {
            String kayttajatunnus = rs.getString("kayttajatunnus");
            kayttajat.add(new Kayttaja(kayttajatunnus));
        }

        rs.close();
        stmt.close();
        connection.close();

        return kayttajat;
    }

    @Override
    public List<Kayttaja> findAllIn(Collection<String> keys) throws SQLException {
        if (keys.isEmpty()) {
            return new ArrayList<>();
        }

        StringBuilder muuttujat = new StringBuilder("?");
        for (int i = 0; i < keys.size(); i++) {
            muuttujat.append(", ?");
        }

        Connection connection = database.getConnection();

        String query = "SELECT * FROM Kayttaja WHERE kayttajatunnus IN (" + muuttujat + ")";
        PreparedStatement stmt = connection.prepareStatement(query);

        int laskuri = 1;
        for (String key : keys) {
            stmt.setObject(laskuri, key);
            laskuri++;
        }

        ResultSet rs = stmt.executeQuery();
        List<Kayttaja> kayttajat = new ArrayList<>();

        while (rs.next()) {
            String kayttajatunnus = rs.getString("kayttajatunnus");
            kayttajat.add(new Kayttaja(kayttajatunnus));
        }

        rs.close();
        stmt.close();
        connection.close();

        return kayttajat;
    }

    @Override
    public void delete(String key) throws SQLException {
        Connection connection = database.getConnection();

        String query = "DELETE FROM Kayttaja WHERE kayttajatunnus = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, key);

        stmt.executeUpdate();
        
        stmt.close();
        connection.close();                       
    }

    @Override
    public List<Kayttaja> findAllWhereXIsK(String x, String key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(String updateQuery, Object... params) throws SQLException {
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
