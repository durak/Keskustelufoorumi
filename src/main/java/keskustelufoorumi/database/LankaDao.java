package keskustelufoorumi.database;

import java.util.*;
import java.sql.*;
import keskustelufoorumi.domain.Alue;
import keskustelufoorumi.domain.Lanka;

public class LankaDao implements Dao<Lanka, Integer> {

    private Database database;
    private AlueDao alueDao;

    public LankaDao(Database database, AlueDao alueDao) {
        this.database = database;
        this.alueDao = alueDao;
    }

    public int getMaxId() throws SQLException {
        Connection connection = database.getConnection();
        String query = "SELECT max(id) FROM Lanka;";

        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();

        boolean hasOne = rs.next();
        if (!hasOne) {
            return -1;
        }

        int maxId = rs.getInt("max(id)");

        rs.close();
        stmt.close();
        connection.close();

        return maxId;
    }

    @Override
    public Lanka findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();

        String query = "SELECT * FROM Lanka WHERE id = ?;";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        int id = rs.getInt("id");
        String lankanimi = rs.getString("lankanimi");
        int alueId = rs.getInt("alue_id");
        int lankaviestimaara = rs.getInt("lankaviestimaara");
        Timestamp viimeisinAika = rs.getTimestamp("viimeisin_aika");
        Lanka l = new Lanka(id, lankanimi, alueDao.findOne(alueId), lankaviestimaara, viimeisinAika);

        rs.close();
        stmt.close();
        connection.close();

        return l;
    }

    @Override
    public List<Lanka> findAll() throws SQLException {
        Connection connection = database.getConnection();

        String query = "SELECT * FROM Lanka;";
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        List<Lanka> langat = new ArrayList<>();

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

    @Override
    public List<Lanka> findAllIn(Collection<Integer> keys) throws SQLException {
        if (keys.isEmpty()) {
            return new ArrayList<>();
        }

        StringBuilder muuttujat = new StringBuilder("?");
        for (int i = 0; i < keys.size(); i++) {
            muuttujat.append(", ?");
        }

        Connection connection = database.getConnection();
        String query = "SELECT * FROM Lanka WHERE id IN (" + muuttujat + ");";
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();
        List<Lanka> langat = new ArrayList<>();

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
        
      
    public int findCountOfLankaInAlue(int alueId) throws SQLException {        
        Connection connection = database.getConnection();
        String query = "SELECT count(*) FROM Lanka WHERE alue_id = ?;";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, alueId);
        
        ResultSet rs = stmt.executeQuery();
                         
        boolean hasOne = rs.next();
        if (!hasOne) {
            return -1;
        }
        
        int count = rs.getInt("count(*)");
        
        rs.close();
        stmt.close();
        connection.close();
                
        return count;
    }
    
    
    
    
    
    
//    public List<Lanka> findAllWithAlueId(Integer key) throws SQLException {
//        Connection connection = database.getConnection();
//        String query = "SELECT * FROM Lanka WHERE alue_id = ?";
//        PreparedStatement stmt = connection.prepareStatement(query);
//        stmt.setObject(1, key);
//        
//        ResultSet rs = stmt.executeQuery();
//        List<Lanka> langat = new ArrayList<>();
//
//        while (rs.next()) {
//            int id = rs.getInt("id");
//            String lankanimi = rs.getString("lankanimi");
//            int lankaviestimaara = rs.getInt("lankaviestimaara");
//            int alueId = rs.getInt("alue_id");
//            Timestamp viimeisinAika = rs.getTimestamp("viimeisin_aika");
//            langat.add(new Lanka(id, lankanimi, alueDao.findOne(alueId), lankaviestimaara, viimeisinAika));
//        }
//
//        rs.close();
//        stmt.close();
//        connection.close();
//
//        return langat;
//    }
    /*
     * järjestetty ja limitoitu versio
     */
    private List<Lanka> findAllWithQueryAndParams(String query, Object... params) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        
        
        
        ResultSet rs = stmt.executeQuery();
        List<Lanka> langat = new ArrayList<>();
        
        
        
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
    
    public List<Lanka> findTenInAlueIdWithOffset(int alueId, int offset) throws SQLException {
        String query = "SELECT * FROM Lanka WHERE alue_id = ? ORDER BY viimeisin_aika DESC LIMIT 10 OFFSET ?;";
        Object[] params = {alueId, offset * 10};
        
        return findAllWithQueryAndParams(query, params);
    }

    public List<Lanka> findAllWithAlueId(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        String query = "SELECT * FROM Lanka WHERE alue_id = ? ORDER BY viimeisin_aika DESC;";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        List<Lanka> langat = new ArrayList<>();

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

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();

        String query = "DELETE FROM Lanka WHERE id = ?;";
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

    public void insertNewLanka(Lanka lanka) throws SQLException {
        String updateQuery = "INSERT INTO Lanka (lankanimi, alue_id, lankaviestimaara, viimeisin_aika) VALUES (?, ?, ?, ?);";
        Object[] params = {lanka.getLankanimi(), lanka.getAlue().getId(), lanka.getLankaviestimaara(), lanka.getViimeisinAika()};
        update(updateQuery, params);
    }

    public void updateLanka(Lanka lanka) throws SQLException {
        String updateQuery = "UPDATE Lanka SET lankaviestimaara = ?, viimeisin_aika = ? WHERE id = ?;";
        Object[] params = {lanka.getLankaviestimaara(), lanka.getViimeisinAika(), lanka.getId()};
        update(updateQuery, params);
    }

    @Override
    public void insertNewInstance(Lanka lanka) throws SQLException {
        String updateQuery = "INSERT INTO Lanka (lankanimi, alue_id, lankaviestimaara, viimeisin_aika) VALUES (?, ?, ?, ?);";
        Object[] params = {lanka.getLankanimi(), lanka.getAlue().getId(), lanka.getLankaviestimaara(), lanka.getViimeisinAika()};
        update(updateQuery, params);
    }

    @Override
    public void updateInstance(Lanka lanka) throws SQLException {
        String updateQuery = "UPDATE Lanka SET lankaviestimaara = ?, viimeisin_aika = ? WHERE id = ?;";
        Object[] params = {lanka.getLankaviestimaara(), lanka.getViimeisinAika(), lanka.getId()};
        update(updateQuery, params);
    }

}
