package keskustelufoorumi.database;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import keskustelufoorumi.domain.Kayttaja;

public interface KayttajaDao {

    // Käyttäjän haku taulusta
    Kayttaja findOne(String id) throws SQLException;

    // Uusi käyttäjä tauluun
    void insertNewKayttaja(Kayttaja kayttaja) throws SQLException;

    //    List<Kayttaja> findAll() throws SQLException;
    //    List<Kayttaja> findAllIn(Collection<String> keys) throws SQLException;
    //    void delete(String key) throws SQLException;
    //    void updateInstance(Kayttaja kayttaja) throws SQLException;    
}
