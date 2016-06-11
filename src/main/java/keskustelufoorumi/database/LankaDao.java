package keskustelufoorumi.database;
import java.util.*;
import java.sql.*;
import keskustelufoorumi.domain.Lanka;

public class LankaDao implements Dao<Lanka, String> {

    private Database database;
    private AlueDao alueDao;

    public LankaDao(Database database, AlueDao alueDao) {
        this.database = database;
        this.alueDao = alueDao;
    }

    @Override
    public Lanka findOne(String key) throws SQLException {
        Connection connection = database.getConnection();

        String query = "SELECT * FROM Lanka WHERE lankanimi = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        String lankanimi = rs.getString("lankanimi");
        String alue = rs.getString("alue");
        int lankaviestimaara = rs.getInt("lankaviestimaara");
        Lanka l = new Lanka(lankanimi, alueDao.findOne(alue), lankaviestimaara);

        rs.close();
        stmt.close();
        connection.close();

        return l;
    }

    @Override
    public List<Lanka> findAll() throws SQLException {
        Connection connection = database.getConnection();

        String query = "SELECT * FROM Lanka";
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        List<Lanka> langat = new ArrayList<>();

        while (rs.next()) {
            String lankanimi = rs.getString("lankanimi");
            int lankaviestimaara = rs.getInt("lankaviestimaara");
            String alue = rs.getString("alue");
            langat.add(new Lanka(lankanimi, alueDao.findOne(alue), lankaviestimaara));
        }

        rs.close();
        stmt.close();
        connection.close();

        return langat;
    }

    @Override
    public List<Lanka> findAllIn(Collection<String> keys) throws SQLException {
        if (keys.isEmpty()) {
            return new ArrayList<>();
        }

        StringBuilder muuttujat = new StringBuilder("?");
        for (int i = 0; i < keys.size(); i++) {
            muuttujat.append(", ?");
        }

        Connection connection = database.getConnection();

        String query = "SELECT * FROM Lanka WHERE lankanimi IN (" + muuttujat + ")";
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        List<Lanka> langat = new ArrayList<>();

        while (rs.next()) {
            String lankanimi = rs.getString("lankanimi");
            int lankaviestimaara = rs.getInt("lankaviestimaara");
            String alue = rs.getString("alue");
            langat.add(new Lanka(lankanimi, alueDao.findOne(alue), lankaviestimaara));
        }

        rs.close();
        stmt.close();
        connection.close();

        return langat;
    }

    @Override
    public List<Lanka> findAllWhereXIsK(String x, String key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(String key) throws SQLException {
        Connection connection = database.getConnection();

        String query = "DELETE FROM Lanka WHERE lankanimi = ?";
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
