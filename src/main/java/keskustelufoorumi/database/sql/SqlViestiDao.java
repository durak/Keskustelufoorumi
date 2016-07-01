package keskustelufoorumi.database.sql;

import java.util.*;
import java.sql.*;
import keskustelufoorumi.database.Database;
import keskustelufoorumi.database.ViestiDao;
import keskustelufoorumi.domain.Viesti;

public class SqlViestiDao implements ViestiDao {

    private Database database;
    private SqlKayttajaDao kayttajaDao;
    private SqlLankaDao lankaDao;

    public SqlViestiDao(Database database, SqlKayttajaDao kayttajaDao, SqlLankaDao lankaDao) {
        this.database = database;
        this.kayttajaDao = kayttajaDao;
        this.lankaDao = lankaDao;
    }

    @Override
    public List<Viesti> findViestitInLanka(int lankaId, int haettavaLkm, int offset) throws SQLException {
        String query = "SELECT * FROM Viesti WHERE lanka_id = ? ORDER BY lahetysaika ASC LIMIT ? OFFSET ?;";
        Object[] params = {lankaId, haettavaLkm, offset * 15};

        return findAllWithQueryAndParams(query, params);
    }

    @Override
    public List<Viesti> findViestitWithKayttajaId(String kayttajaId) throws SQLException {
        String query = "SELECT * FROM Viesti WHERE kayttaja_id = ?;";
        Object[] params = {kayttajaId};

        List<Viesti> viestit = findAllWithQueryAndParams(query, params);

        return viestit;
    }

    @Override
    public int findCountOfViestiInLanka(int lankaId) throws SQLException {
        Connection connection = database.getConnection();
        String query = "SELECT count(*) AS lkm FROM Viesti WHERE lanka_id = ?;";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, lankaId);

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
        String query = "SELECT max(id) AS max_id FROM Viesti;";

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
    public void insertNewViesti(Viesti viesti) throws SQLException {
        String updateQuery = "INSERT INTO Viesti (sisalto, kayttaja_id, lanka_id, lahetysaika) VALUES (?, ?, ?, ?);";
        Object[] params = {viesti.getSisalto(), viesti.getKayttaja().getId(), viesti.getLanka().getId(), viesti.getLahetysaika()};
        update(updateQuery, params);
    }

    /*
     * Apumetodit
     * findAllWithQueryAndParams: lukukyselyt
     * update: kirjoittavat kyselyt
     */
    private List<Viesti> findAllWithQueryAndParams(String query, Object... params) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();

        while (rs.next()) {
            int id = rs.getInt("id");
            String sisalto = rs.getString("sisalto");
            String kayttajaId = rs.getString("kayttaja_id");
            int lankaId = rs.getInt("lanka_id");
            Timestamp lahetysaika = rs.getTimestamp("lahetysaika");

            viestit.add(new Viesti(id, sisalto, kayttajaDao.findOne(kayttajaId), lankaDao.findOne(lankaId), lahetysaika));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
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

    /*
     * Käytöstä poistetut metodit
     */
    //    @Override
//    public List<Viesti> findAll() throws SQLException {
//        Connection connection = database.getConnection();
//
//        String query = "SELECT * FROM Viesti;";
//        PreparedStatement stmt = connection.prepareStatement(query);
//
//        ResultSet rs = stmt.executeQuery();
//        List<Viesti> viestit = new ArrayList<>();
//
//        while (rs.next()) {
//            int id = rs.getInt("id");
//            String sisalto = rs.getString("sisalto");
//            String kayttajaId = rs.getString("kayttaja_id");
//            int lankaId = rs.getInt("lanka_id");
//            Timestamp lahetysaika = rs.getTimestamp("lahetysaika");
//
//            viestit.add(new Viesti(id, sisalto, kayttajaDao.findOne(kayttajaId), lankaDao.findOne(lankaId), lahetysaika));
//        }
//
//        rs.close();
//        stmt.close();
//        connection.close();
//
//        return viestit;
//    }
//    @Override
//    public List<Viesti> findAllIn(Collection<Integer> keys) throws SQLException {
//        if (keys.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        StringBuilder muuttujat = new StringBuilder("?");
//        for (int i = 0; i < keys.size(); i++) {
//            muuttujat.append(", ?");
//        }
//
//        Connection connection = database.getConnection();
//        String query = "SELECT * FROM Viesti WHERE id IN (" + muuttujat + ");";
//        PreparedStatement stmt = connection.prepareStatement(query);
//
//        ResultSet rs = stmt.executeQuery();
//        List<Viesti> viestit = new ArrayList<>();
//
//        while (rs.next()) {
//            int id = rs.getInt("id");
//            String sisalto = rs.getString("sisalto");
//            String kayttajaId = rs.getString("kayttaja_id");
//            int lankaId = rs.getInt("lanka_id");
//            Timestamp lahetysaika = rs.getTimestamp("lahetysaika");
//
//            viestit.add(new Viesti(id, sisalto, kayttajaDao.findOne(kayttajaId), lankaDao.findOne(lankaId), lahetysaika));
//        }
//
//        rs.close();
//        stmt.close();
//        connection.close();
//
//        return viestit;
//    }
    //    @Override
//    public void delete(Integer key) throws SQLException {
//        Connection connection = database.getConnection();
//
//        String query = "DELETE FROM Viesti WHERE id = ?;";
//        PreparedStatement stmt = connection.prepareStatement(query);
//        stmt.setObject(1, key);
//
//        stmt.executeUpdate();
//
//        stmt.close();
//        connection.close();
//    }
    //    @Override
//    public List<Viesti> findAllWithLankaId(Integer lankaId) throws SQLException {
//
//        Connection connection = database.getConnection();
//        String query = "SELECT * FROM Viesti WHERE lanka_id = ?;";
//        PreparedStatement stmt = connection.prepareStatement(query);
//        stmt.setObject(1, lankaId);
//
//        ResultSet rs = stmt.executeQuery();
//        List<Viesti> viestit = new ArrayList<>();
//
//        while (rs.next()) {
//            int id = rs.getInt("id");
//            String sisalto = rs.getString("sisalto");
//            String kayttajaId = rs.getString("kayttaja_id");
//            lankaId = rs.getInt("lanka_id");
//            Timestamp lahetysaika = rs.getTimestamp("lahetysaika");
//
//            viestit.add(new Viesti(id, sisalto, kayttajaDao.findOne(kayttajaId), lankaDao.findOne(lankaId), lahetysaika));
//        }
//
//        rs.close();
//        stmt.close();
//        connection.close();
//
//        return viestit;
//    }
    //    @Override
//    public Viesti findOne(Integer key) throws SQLException {
//        Connection connection = database.getConnection();
//
//        String query = "SELECT * FROM Viesti WHERE id = ?;";
//        PreparedStatement stmt = connection.prepareStatement(query);
//        stmt.setObject(1, key);
//
//        ResultSet rs = stmt.executeQuery();
//        boolean hasOne = rs.next();
//        if (!hasOne) {
//            return null;
//        }
//
//        int id = rs.getInt("id");
//        String sisalto = rs.getString("sisalto");
//        String kayttajaId = rs.getString("kayttaja_id");
//        int lankaId = rs.getInt("lanka_id");
//        Timestamp lahetysaika = rs.getTimestamp("lahetysaika");
//
//        Viesti viesti = new Viesti(id, sisalto, kayttajaDao.findOne(kayttajaId), lankaDao.findOne(lankaId), lahetysaika);
//
//        rs.close();
//        stmt.close();
//        connection.close();
//
//        return viesti;
//    }
    //    @Override
//    public void updateViesti(Viesti viesti) throws SQLException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}
