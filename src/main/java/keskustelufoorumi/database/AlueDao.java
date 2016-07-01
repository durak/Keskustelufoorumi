package keskustelufoorumi.database;

import keskustelufoorumi.domain.Alue;
import java.sql.SQLException;
import java.util.List;

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

}
