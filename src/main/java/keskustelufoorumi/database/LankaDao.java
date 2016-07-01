package keskustelufoorumi.database;

import keskustelufoorumi.domain.Lanka;
import java.sql.SQLException;
import java.util.List;

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

}
