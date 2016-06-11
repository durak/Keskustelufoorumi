package keskustelufoorumi.database;

import java.sql.*;
import java.util.*;
import keskustelufoorumi.domain.Alue;

public class AlueDao implements Dao<Alue, String> {

    private Database database;

    public AlueDao(Database database) {
        this.database = database;
    }

    @Override
    public Alue findOne(String key) throws SQLException {
        Connection connection = database.getConnection();

        String query = "SELECT * FROM Alue WHERE aluenimi = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        String aluenimi = rs.getString("aluenimi");
        int alueviestimaara = rs.getInt("alueviestimaara");
        Alue a = new Alue(aluenimi, alueviestimaara);

        rs.close();
        stmt.close();
        connection.close();

        return a;
    }

    @Override
    public List<Alue> findAll() throws SQLException {
        Connection connection = database.getConnection();

        String query = "SELECT * FROM Alue";
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        List<Alue> alueet = new ArrayList<>();

        while (rs.next()) {
            String aluenimi = rs.getString("aluenimi");
            int alueviestimaara = rs.getInt("alueviestimaara");
            alueet.add(new Alue(aluenimi, alueviestimaara));
        }

        rs.close();
        stmt.close();
        connection.close();

        return alueet;
    }

    @Override
    public List<Alue> findAllIn(Collection<String> keys) throws SQLException {
        if (keys.isEmpty()) {
            return new ArrayList<>();
        }

        StringBuilder muuttujat = new StringBuilder("?");
        for (int i = 0; i < keys.size(); i++) {
            muuttujat.append(", ?");
        }

        Connection connection = database.getConnection();

        String query = "SELECT * FROM Alue WHERE aluenimi IN (" + muuttujat + ")";
        PreparedStatement stmt = connection.prepareStatement(query);

        int laskuri = 1;
        for (String key : keys) {
            stmt.setObject(laskuri, key);
            laskuri++;
        }

        ResultSet rs = stmt.executeQuery();
        List<Alue> alueet = new ArrayList<>();

        while (rs.next()) {
            String aluenimi = rs.getString("aluenimi");
            int alueviestimaara = rs.getInt("alueviestimaara");
            alueet.add(new Alue(aluenimi, alueviestimaara));
        }

        rs.close();
        stmt.close();
        connection.close();

        return alueet;
    }

    @Override
    public List<Alue> findAllWhereXIsK(String x, String key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(String key) throws SQLException {
        Connection connection = database.getConnection();

        String query = "DELETE FROM Alue WHERE aluenimi = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, key);

        stmt.executeUpdate();

        stmt.close();
        connection.close();
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
