package keskustelufoorumi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import keskustelufoorumi.domain.Alue;
import keskustelufoorumi.domain.Kayttaja;
import keskustelufoorumi.domain.Lanka;
import keskustelufoorumi.domain.Viesti;

public class DaoManager {

    private final String databaseAddress;
    private Database database;
    private Connection connection = null;
    protected AlueDao alueDao = null;
    protected LankaDao lankaDao = null;
    protected ViestiDao viestiDao = null;
    protected KayttajaDao kayttajaDao = null;

    public DaoManager() {
        this.databaseAddress = "jdbc:sqlite:forum.db";
    }

    /*
    YHTEYDENPITO TIETOKANTAAN
     */
    private Connection getConnection() throws SQLException {
        if (this.connection == null) {
            this.connection = DriverManager.getConnection(databaseAddress);
        }
        
        return this.connection;
    }

    private Connection getTxConnection() {
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(DaoManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return this.connection;
    }

    /*
    DAO FACTORY
     */
    public KayttajaDao getKayttajaDao() {
        if (this.kayttajaDao == null) {
            this.kayttajaDao = new KayttajaDao(this.connection);
        }

        return this.kayttajaDao;
    }

    public AlueDao getAlueDao() {
        if (this.alueDao == null) {
            this.alueDao = new AlueDao(this.connection);
        }

        return this.alueDao;
    }

    public LankaDao getLankaDao() {
        if (this.lankaDao == null) {
            this.lankaDao = new LankaDao(this.connection, getAlueDao());
        }

        return this.lankaDao;
    }

    public ViestiDao getViestiDao() {
        if (this.viestiDao == null) {
            this.viestiDao = new ViestiDao(this.connection, getKayttajaDao(), getLankaDao());
        }

        return this.viestiDao;
    }

    /*
    Multi-Dao transaktiot
     */
    public void teePaivitys(Viesti viesti) throws SQLException {

        try {

            getConnection().setAutoCommit(false);

            Kayttaja kayttaja = viesti.getKayttaja();
            Lanka lanka = viesti.getLanka();
            Alue alue = lanka.getAlue();

            // uusi käyttäjä
            if (getKayttajaDao().findOne(kayttaja.getId()) == null) {
                getKayttajaDao().insertNewKayttaja(kayttaja);
            }

            // uusi alue / paivitys
            if (getAlueDao().findOne(alue.getId()) == null) {
                getAlueDao().insertNewAlue(alue);
            } else {
                getAlueDao().updateAlue(alue);
            }
            
            // uusi lanka / päivitys
            if (getLankaDao().findOne(lanka.getId()) == null) {
                getLankaDao().insertNewLanka(lanka);
            } else {
                getLankaDao().updateLanka(lanka);
            }

            getViestiDao().insertNewViesti(viesti);

            this.connection.commit();

        } catch (Exception e) {
            if (this.connection != null) {
                this.connection.rollback();
            }
        } finally {
            if (this.connection != null) {
                this.connection.setAutoCommit(true);
                this.connection.close();

            }

        }

    }
}
