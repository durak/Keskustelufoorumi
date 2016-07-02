package keskustelufoorumi.database.sql;

import java.sql.*;
import java.util.*;
import keskustelufoorumi.database.AlueDao;
import keskustelufoorumi.database.Database;
import keskustelufoorumi.domain.Alue;

public class SqlAlueDao implements AlueDao {

    private Database database;

    public SqlAlueDao(Database database) {
        this.database = database;

    }

    @Override
    public Alue findOne(int id) throws SQLException {
        Connection connection = database.getConnection();
        String query = "SELECT * FROM Alue WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, id);
        ResultSet rs = stmt.executeQuery();

        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        id = rs.getInt("id");
        String aluenimi = rs.getString("aluenimi");
        int alueviestimaara = rs.getInt("alueviestimaara");
        Timestamp viimeisinAika = rs.getTimestamp("viimeisin_aika");
        Alue a = new Alue(id, aluenimi, alueviestimaara, viimeisinAika);

        rs.close();
        stmt.close();
        connection.close();

        return a;
    }

    @Override
    public List<Alue> findAll() throws SQLException {
        Connection connection = database.getConnection();
        String query = "SELECT * FROM Alue ORDER BY id ASC;";
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
        connection.close();

        return alueet;
    }

    @Override
    public int findLatestId() throws SQLException {
        Connection connection = database.getConnection();
        String query = "SELECT max(id) AS max_id FROM Alue;";
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        boolean hasOne = rs.next();
        if (!hasOne) {
            return -1;
        }

        int maxId = rs.getInt("max_id");

        rs.close();
        stmt.close();
        connection.close();

        return maxId;
    }

    @Override
    public void insertNewInstance(Alue alue) throws SQLException {
        String updateQuery = "INSERT INTO Alue (aluenimi, alueviestimaara, viimeisin_aika) VALUES (?, ?, ?);";
        Object[] params = {alue.getAluenimi(), alue.getAlueviestimaara(), alue.getViimeisinAika()};
        update(updateQuery, params);
    }

    @Override
    public void updateInstance(Alue alue) throws SQLException {
        String updateQuery = "UPDATE Alue SET alueviestimaara = ?, viimeisin_aika = ? WHERE id = ?;";
        Object[] params = {alue.getAlueviestimaara(), alue.getViimeisinAika(), alue.getId()};
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
