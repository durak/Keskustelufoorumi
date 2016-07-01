package keskustelufoorumi.database;

import keskustelufoorumi.domain.Kayttaja;
import java.sql.SQLException;

public interface KayttajaDao {

    // Käyttäjän haku taulusta
    Kayttaja findOne(String id) throws SQLException;

    // Uusi käyttäjä tauluun
    void insertNewKayttaja(Kayttaja kayttaja) throws SQLException;

}
