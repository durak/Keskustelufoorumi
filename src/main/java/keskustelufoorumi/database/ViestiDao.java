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

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();

        String query = "SELECT * FROM Viesti WHERE viestitunnus = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        int viestitunnus = rs.getInt("viestitunnus");
        String sisalto = rs.getString("sisalto");
        String kayttaja = rs.getString("kayttaja");
        String lankanimi = rs.getString("lankanimi");
        String lahetysaika = rs.getString("lahetysaika");

        Viesti v = new Viesti(viestitunnus, sisalto, kayttajaDao.findOne(kayttaja), lankaDao.findOne(lankanimi), lahetysaika);

        rs.close();
        stmt.close();
        connection.close();

        return v;
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        Connection connection = database.getConnection();

        String query = "SELECT * FROM Viesti";
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();

        while (rs.next()) {
            int viestitunnus = rs.getInt("viestitunnus");
            String sisalto = rs.getString("sisalto");
            String kayttaja = rs.getString("kayttaja");
            String lankanimi = rs.getString("lankanimi");
            String lahetysaika = rs.getString("lahetysaika");

            viestit.add(new Viesti(viestitunnus, sisalto, kayttajaDao.findOne(kayttaja), lankaDao.findOne(lankanimi), lahetysaika));
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

        String query = "SELECT * FROM Viesti WHERE viestitunnus IN (" + muuttujat + ")";
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();

        while (rs.next()) {
            int viestitunnus = rs.getInt("viestitunnus");
            String sisalto = rs.getString("sisalto");
            String kayttaja = rs.getString("kayttaja");
            String lankanimi = rs.getString("lankanimi");
            String lahetysaika = rs.getString("lahetysaika");

            viestit.add(new Viesti(viestitunnus, sisalto, kayttajaDao.findOne(kayttaja), lankaDao.findOne(lankanimi), lahetysaika));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }

    @Override
    public List<Viesti> findAllWhereXIsK(String x, Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();

        String query = "DELETE FROM Viesti WHERE viestitunnus = ?";
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
