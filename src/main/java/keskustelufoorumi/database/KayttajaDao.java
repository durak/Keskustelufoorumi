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

        String query = "SELECT * FROM Kayttaja WHERE id = ?;";
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

    @Override
    public List<Kayttaja> findAll() throws SQLException {
        Connection connection = database.getConnection();

        String query = "SELECT * FROM Kayttaja;";
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        List<Kayttaja> kayttajat = new ArrayList<>();

        while (rs.next()) {
            String id = rs.getString("id");
            kayttajat.add(new Kayttaja(id));
        }

        rs.close();
        stmt.close();

        connection.close();

        return kayttajat;
    }

    @Override
    public List<Kayttaja> findAllIn(Collection<String> keys) throws SQLException {
        Connection connection = database.getConnection();

        if (keys.isEmpty()) {
            return new ArrayList<>();
        }

        StringBuilder muuttujat = new StringBuilder("?");
        for (int i = 0; i < keys.size(); i++) {
            muuttujat.append(", ?");
        }

        String query = "SELECT * FROM Kayttaja WHERE id IN (" + muuttujat + ");";
        PreparedStatement stmt = connection.prepareStatement(query);

        int laskuri = 1;
        for (String key : keys) {
            stmt.setObject(laskuri, key);
            laskuri++;
        }

        ResultSet rs = stmt.executeQuery();
        List<Kayttaja> kayttajat = new ArrayList<>();

        while (rs.next()) {
            String id = rs.getString("id");
            kayttajat.add(new Kayttaja(id));
        }

        rs.close();
        stmt.close();

        connection.close();

        return kayttajat;
    }

    @Override
    public void delete(String key) throws SQLException {
        Connection connection = database.getConnection();

        String query = "DELETE FROM Kayttaja WHERE id = ?;";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, key);

        stmt.executeUpdate();

        stmt.close();

        connection.close();
    }

    
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

    public void insertNewKayttaja(Kayttaja kayttaja) throws SQLException {
        String updateQuery = "INSERT INTO Kayttaja VALUES (?);";
        String[] params = {kayttaja.getId()};
        update(updateQuery, params);
    }

    @Override
    public void updateInstance(Kayttaja kayttaja) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void insertNewInstance(Kayttaja kayttaja) throws SQLException {
        String updateQuery = "INSERT INTO Kayttaja VALUES (?);";
        String[] params = {kayttaja.getId()};
        update(updateQuery, params);
    }

}
