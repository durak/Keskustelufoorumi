package keskustelufoorumi.database;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import keskustelufoorumi.domain.Viesti;

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

//    void delete(Integer key) throws SQLException;
    //    List<Viesti> findAll() throws SQLException;
    //    List<Viesti> findAllIn(Collection<Integer> keys) throws SQLException;
    //    List<Viesti> findAllWithLankaId(Integer lankaId) throws SQLException;
    //    Viesti findOne(Integer key) throws SQLException;
    //    void updateViesti(Viesti viesti) throws SQLException;
}
