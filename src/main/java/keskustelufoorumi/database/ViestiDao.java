package keskustelufoorumi.database;

import java.util.*;
import java.sql.*;
import keskustelufoorumi.domain.Lanka;
import keskustelufoorumi.domain.Viesti;

public class ViestiDao implements Dao<Viesti, Integer> {

    private Database database;
    private KayttajaDao kayttajaDao;
    private LankaDao lankaDao;

    public ViestiDao(Database database, KayttajaDao kayttajaDao, LankaDao lankaDao) {
        this.database = database;
        this.kayttajaDao = kayttajaDao;
        this.lankaDao = lankaDao;
    }

    public int getMaxId() throws SQLException {
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
    public Viesti findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();

        String query = "SELECT * FROM Viesti WHERE id = ?;";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        int id = rs.getInt("id");
        String sisalto = rs.getString("sisalto");
        String kayttajaId = rs.getString("kayttaja_id");
        int lankaId = rs.getInt("lanka_id");
        Timestamp lahetysaika = rs.getTimestamp("lahetysaika");

        Viesti v = new Viesti(id, sisalto, kayttajaDao.findOne(kayttajaId), lankaDao.findOne(lankaId), lahetysaika);

        rs.close();
        stmt.close();
        connection.close();

        return v;
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        Connection connection = database.getConnection();

        String query = "SELECT * FROM Viesti;";
        PreparedStatement stmt = connection.prepareStatement(query);

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

    @Override
    public List<Viesti> findAllIn(Collection<Integer> keys) throws SQLException {
        if (keys.isEmpty()) {
            return new ArrayList<>();
        }

        StringBuilder muuttujat = new StringBuilder("?");
        for (int i = 0; i < keys.size(); i++) {
            muuttujat.append(", ?");
        }

        Connection connection = database.getConnection();
        String query = "SELECT * FROM Viesti WHERE id IN (" + muuttujat + ");";
        PreparedStatement stmt = connection.prepareStatement(query);

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

    public List<Viesti> findAllWithLankaId(Integer key) throws SQLException {

        Connection connection = database.getConnection();
        String query = "SELECT * FROM Viesti WHERE lanka_id = ?;";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, key);

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

    public List<Viesti> find15WithLankaId(int lankaId, int offset) throws SQLException {
        String query = "SELECT * FROM Viesti WHERE lanka_id = ? ORDER BY lahetysaika ASC LIMIT 15 OFFSET ?;";
        Object[] params = {lankaId, offset * 15};

        return findAllWithQueryAndParams(query, params);
    }

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

    public List<Viesti> findAllWithUserId(String userId) throws SQLException {

        Connection connection = database.getConnection();
        String query = "SELECT * FROM Viesti WHERE kayttaja_id = ?;";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, userId);

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

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();

        String query = "DELETE FROM Viesti WHERE id = ?;";
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



    @Override
    public void insertNewInstance(Viesti viesti) throws SQLException {
        String updateQuery = "INSERT INTO Viesti (sisalto, kayttaja_id, lanka_id, lahetysaika) VALUES (?, ?, ?, ?);";
        Object[] params = {viesti.getSisalto(), viesti.getKayttaja().getId(), viesti.getLanka().getId(), viesti.getLahetysaika()};
        update(updateQuery, params);
    }

    @Override
    public void updateInstance(Viesti viesti) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
