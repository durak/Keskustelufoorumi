package keskustelufoorumi.database;

import keskustelufoorumi.domain.Viesti;
import java.sql.SQLException;
import java.util.List;

public interface ViestiDao {

    // Hakee viestijoukkoja langasta (kooltaan haettavaLkm), offset-parametrilla siirretään valinnan alkua
    List<Viesti> findViestitInLanka(int lankaId, int haettavaLkm, int offset) throws SQLException;

    // Kaikki yhden käyttäjän viestit
    List<Viesti> findViestitWithKayttajaId(String kayttajaId) throws SQLException;

    // Viestien määrä tietyssä langassa
    int findCountOfViestiInLanka(int lankaId) throws SQLException;

    // Viimeisimmän viestin Id     
    int findLatestId() throws SQLException;

    // Uusi viesti tauluun
    void insertNewViesti(Viesti viesti) throws SQLException;

}
