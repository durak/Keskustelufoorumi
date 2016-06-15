package keskustelufoorumi.database;

import java.util.*;
import java.sql.*;
import keskustelufoorumi.domain.Lanka;
import keskustelufoorumi.domain.Viesti;

public class ViestiDao implements Dao<Viesti, Integer> {

//    private Database database;
    private Connection connection;
    private KayttajaDao kayttajaDao;
    private LankaDao lankaDao;

    public ViestiDao(Connection connection, KayttajaDao kayttajaDao, LankaDao lankaDao) {
//        this.database = database;
        this.connection = connection;
        this.kayttajaDao = kayttajaDao;
        this.lankaDao = lankaDao;
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
//        Connection connection = database.getConnection();

        String query = "SELECT * FROM Viesti WHERE id = ?";
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
//        connection.close();

        return v;
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
//        Connection connection = database.getConnection();

        String query = "SELECT * FROM Viesti";
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
//        connection.close();

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

//        Connection connection = database.getConnection();

        String query = "SELECT * FROM Viesti WHERE id IN (" + muuttujat + ")";
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
//        connection.close();

        return viestit;
    }

    @Override
    public List<Viesti> findAllWhereXIsK(String x, Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
//        Connection connection = database.getConnection();

        String query = "DELETE FROM Viesti WHERE id = ?";
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
    
    public void insertNewViesti(Viesti viesti) throws SQLException {
        String updateQuery = "INSERT INTO Viesti (sisalto, kayttaja_id, lanka_id, lahetysaika) VALUES (?, ?, ?, ?)";
        Object[] params = {viesti.getSisalto(), viesti.getKayttaja().getId(), viesti.getLanka().getId(), viesti.getLahetysaika()};
        update(updateQuery, params);
    }

}
