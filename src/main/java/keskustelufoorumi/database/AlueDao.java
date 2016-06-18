package keskustelufoorumi.database;

import java.sql.*;
import java.util.*;
import keskustelufoorumi.domain.Alue;

public class AlueDao implements Dao<Alue, Integer> {

//    private Database database;
    private Connection connection;

    public AlueDao(Connection connection) {
//        this.database = database;
        this.connection = connection;
    }

    @Override
    public Alue findOne(Integer key) throws SQLException {
//        Connection connection = database.getConnection();

        String query = "SELECT * FROM Alue WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        int id = rs.getInt("id");
        String aluenimi = rs.getString("aluenimi");
        int alueviestimaara = rs.getInt("alueviestimaara");
        Timestamp viimeisinAika = rs.getTimestamp("viimeisin_aika");
        Alue a = new Alue(id, aluenimi, alueviestimaara, viimeisinAika);

        rs.close();
        stmt.close();
//        connection.close();

        return a;
    }

    @Override
    public List<Alue> findAll() throws SQLException {
//        Connection connection = database.getConnection();

        String query = "SELECT * FROM Alue";
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        List<Alue> alueet = new ArrayList<>();

        while (rs.next()) {
            int id = rs.getInt("id");
            String aluenimi = rs.getString("aluenimi");
            int alueviestimaara = rs.getInt("alueviestimaara");
            Timestamp viimeisinAika = rs.getTimestamp("viimeisin_aika");
            alueet.add(new Alue(id, aluenimi, alueviestimaara, viimeisinAika));
        }

        rs.close();
        stmt.close();
//        connection.close();

        return alueet;
    }

    @Override
    public List<Alue> findAllIn(Collection<Integer> keys) throws SQLException {
        if (keys.isEmpty()) {
            return new ArrayList<>();
        }

        StringBuilder muuttujat = new StringBuilder("?");
        for (int i = 0; i < keys.size(); i++) {
            muuttujat.append(", ?");
        }

//        Connection connection = database.getConnection();
        String query = "SELECT * FROM Alue WHERE id IN (" + muuttujat + ")";
        PreparedStatement stmt = connection.prepareStatement(query);

        int laskuri = 1;
        for (Integer key : keys) {
            stmt.setObject(laskuri, key);
            laskuri++;
        }

        ResultSet rs = stmt.executeQuery();
        List<Alue> alueet = new ArrayList<>();

        while (rs.next()) {
            int id = rs.getInt("id");
            String aluenimi = rs.getString("aluenimi");
            int alueviestimaara = rs.getInt("alueviestimaara");
            Timestamp viimeisinAika = rs.getTimestamp("viimeisin_aika");
            alueet.add(new Alue(id, aluenimi, alueviestimaara, viimeisinAika));
        }

        rs.close();
        stmt.close();
//        connection.close();

        return alueet;
    }

    @Override
    public List<Alue> findAllWhereXIsK(String x, Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
//        Connection connection = database.getConnection();

        String query = "DELETE FROM Alue WHERE aluenimi = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, key);

        stmt.executeUpdate();

        stmt.close();
//        connection.close();
    }

    @Override
    public void update(String updateQuery, Object... params) throws SQLException {
//        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(updateQuery);

        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        stmt.executeUpdate();

        stmt.close();
//        connection.close();
    }

    public void insertNewAlue(Alue alue) throws SQLException {
        String updateQuery = "INSERT INTO Alue (aluenimi, alueviestimaara, viimeisin_aika) VALUES (?, ?, ?)";
        Object[] params = {alue.getAluenimi(), alue.getAlueviestimaara(), alue.getViimeisinAika()};
        update(updateQuery, params);
    }

    public void updateAlue(Alue alue) throws SQLException {
        String updateQuery = "UPDATE Alue SET alueviestimaara = ?, viimeisin_aika = ? WHERE id = ?";
        Object[] params = {alue.getAlueviestimaara(), alue.getViimeisinAika(), alue.getId()};
        update(updateQuery, params);
    }

    @Override
    public void insertNewInstance(Alue alue) throws SQLException {
        String updateQuery = "INSERT INTO Alue (aluenimi, alueviestimaara, viimeisin_aika) VALUES (?, ?, ?)";
        Object[] params = {alue.getAluenimi(), alue.getAlueviestimaara(), alue.getViimeisinAika()};
        update(updateQuery, params);
    }

    @Override
    public void updateInstance(Alue alue) throws SQLException {
        String updateQuery = "UPDATE Alue SET alueviestimaara = ?, viimeisin_aika = ? WHERE id = ?";
        Object[] params = {alue.getAlueviestimaara(), alue.getViimeisinAika(), alue.getId()};
        update(updateQuery, params);
    }

}
