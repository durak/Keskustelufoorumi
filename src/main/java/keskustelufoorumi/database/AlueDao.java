package keskustelufoorumi.database;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import keskustelufoorumi.domain.Alue;

public interface AlueDao {

    // Hakee yhden alueen taulusta
    Alue findOne(int id) throws SQLException;

    // Hakee kaikki alueet taulusta
    List<Alue> findAll() throws SQLException;

    // Viimeisin id taulusta
    int findLatestId() throws SQLException;

    // Uusi alue tauluun
    void insertNewInstance(Alue alue) throws SQLException;

    // Päivitetään tauluun alueen tiedot
    void updateInstance(Alue alue) throws SQLException;

    //    void delete(Integer key) throws SQLException;
    //    List<Alue> findAllIn(Collection<Integer> keys) throws SQLException;
}
