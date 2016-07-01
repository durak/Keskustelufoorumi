package keskustelufoorumi.database.sql;

import java.util.*;
import java.sql.*;
import keskustelufoorumi.database.Database;
import keskustelufoorumi.database.LankaDao;
import keskustelufoorumi.domain.Lanka;

public class SqlLankaDao implements LankaDao {

    private Database database;
    private SqlAlueDao alueDao;

    public SqlLankaDao(Database database, SqlAlueDao alueDao) {
        this.database = database;
        this.alueDao = alueDao;
    }

    @Override
    public Lanka findOne(int lankaId) throws SQLException {
        String query = "SELECT * FROM Lanka WHERE id = ?;";
        Object[] params = {lankaId};
        List<Lanka> hakutulos = findAllWithQueryAndParams(query, params);

        if (hakutulos.isEmpty()) {
            return null;
        }

        return hakutulos.get(0);
    }

    @Override
    public List<Lanka> findLangatInAlue(int alueId, int haettavaLkm, int offset) throws SQLException {
        String query = "SELECT * FROM Lanka WHERE alue_id = ? ORDER BY viimeisin_aika DESC LIMIT ? OFFSET ?;";
        Object[] params = {alueId, haettavaLkm, offset * 10};

        return findAllWithQueryAndParams(query, params);
    }

    @Override
    public int findCountOfLankaInAlue(int alueId) throws SQLException {
        Connection connection = database.getConnection();
        String query = "SELECT count(*) AS lkm FROM Lanka WHERE alue_id = ?;";

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, alueId);
        ResultSet rs = stmt.executeQuery();

        boolean hasOne = rs.next();
        if (!hasOne) {
            return -1;
        }

        int count = rs.getInt("lkm");

        rs.close();
        stmt.close();
        connection.close();

        return count;
    }

    @Override
    public int findLatestId() throws SQLException {
        Connection connection = database.getConnection();
        String query = "SELECT max(id) AS max_id FROM Lanka;";
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
    public void insertNewLanka(Lanka lanka) throws SQLException {
        String updateQuery = "INSERT INTO Lanka (lankanimi, alue_id, lankaviestimaara, viimeisin_aika) VALUES (?, ?, ?, ?);";
        Object[] params = {lanka.getLankanimi(), lanka.getAlue().getId(), lanka.getLankaviestimaara(), lanka.getViimeisinAika()};
        update(updateQuery, params);
    }

    @Override
    public void updateLanka(Lanka lanka) throws SQLException {
        String updateQuery = "UPDATE Lanka SET lankaviestimaara = ?, viimeisin_aika = ? WHERE id = ?;";
        Object[] params = {lanka.getLankaviestimaara(), lanka.getViimeisinAika(), lanka.getId()};
        update(updateQuery, params);
    }

    /*
     * Apumetodit
     * findAllWithQueryAndParams: lukukyselyt
     * update: kirjoittavat kyselyt
     */
    private List<Lanka> findAllWithQueryAndParams(String query, Object... params) throws SQLException {
        List<Lanka> langat = new ArrayList<>();
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String lankanimi = rs.getString("lankanimi");
            int lankaviestimaara = rs.getInt("lankaviestimaara");
            int alueId = rs.getInt("alue_id");
            Timestamp viimeisinAika = rs.getTimestamp("viimeisin_aika");
            langat.add(new Lanka(id, lankanimi, alueDao.findOne(alueId), lankaviestimaara, viimeisinAika));
        }

        rs.close();
        stmt.close();
        connection.close();

        return langat;
    }

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
