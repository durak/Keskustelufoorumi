package keskustelufoorumi.database;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import keskustelufoorumi.domain.Lanka;

public interface LankaDao {
    
    // Yhden langan haku taulusta
    Lanka findOne(int lankaId) throws SQLException;

    // Lankajouko haku alueelta (joukon koko haettavaLkm), offset siirtää valinnan alkua
    List<Lanka> findLangatInAlue(int alueId, int haettavaLkm, int offset) throws SQLException;
    
    // Lankojen määrä alueella
    int findCountOfLankaInAlue(int alueId) throws SQLException;
    
    // Viimeisin id taulusta
    int findLatestId() throws SQLException;
    
    // Uusi lanka tauluun
    void insertNewLanka(Lanka lanka) throws SQLException;
    
    // Taulussa jo olevan langan tietojen päivitys
    void updateLanka(Lanka lanka) throws SQLException;

//    List<Lanka> findAllIn(Collection<Integer> keys) throws SQLException;
//    void delete(Integer key) throws SQLException;
//    List<Lanka> findAllWithAlueId(Integer alueId) throws SQLException;    
//    List<Lanka> findAll() throws SQLException;
}
