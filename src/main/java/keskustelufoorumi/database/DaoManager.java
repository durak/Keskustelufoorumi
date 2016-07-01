package keskustelufoorumi.database;

import java.sql.SQLException;

public class DaoManager {

    private Database database;
    protected AlueDao alueDao = null;
    protected LankaDao lankaDao = null;
    protected ViestiDao viestiDao = null;
    protected KayttajaDao kayttajaDao = null;

    public DaoManager(Database database) {
        this.database = database;
    }

    public KayttajaDao getKayttajaDao() throws SQLException {
        if (this.kayttajaDao == null) {
            this.kayttajaDao = new KayttajaDao(this.database);
        }

        return this.kayttajaDao;
    }

    public AlueDao getAlueDao() throws SQLException {
        if (this.alueDao == null) {
            this.alueDao = new AlueDao(this.database);
        }

        return this.alueDao;
    }

    public LankaDao getLankaDao() throws SQLException {
        if (this.lankaDao == null) {
            this.lankaDao = new LankaDao(this.database, getAlueDao());
        }

        return this.lankaDao;
    }

    public ViestiDao getViestiDao() throws SQLException {
        if (this.viestiDao == null) {
            this.viestiDao = new ViestiDao(this.database, getKayttajaDao(), getLankaDao());
        }

        return this.viestiDao;
    }

}
