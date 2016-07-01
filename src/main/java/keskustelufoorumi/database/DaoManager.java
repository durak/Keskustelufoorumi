package keskustelufoorumi.database;

import keskustelufoorumi.database.sql.SqlViestiDao;
import keskustelufoorumi.database.sql.SqlLankaDao;
import keskustelufoorumi.database.sql.SqlKayttajaDao;
import keskustelufoorumi.database.sql.SqlAlueDao;
import java.sql.SQLException;

public class DaoManager {

    private Database database;
    protected SqlAlueDao alueDao = null;
    protected SqlLankaDao lankaDao = null;
    protected SqlViestiDao viestiDao = null;
    protected SqlKayttajaDao kayttajaDao = null;

    public DaoManager(Database database) {
        this.database = database;
    }

    public SqlKayttajaDao getKayttajaDao() throws SQLException {
        if (this.kayttajaDao == null) {
            this.kayttajaDao = new SqlKayttajaDao(this.database);
        }

        return this.kayttajaDao;
    }

    public SqlAlueDao getAlueDao() throws SQLException {
        if (this.alueDao == null) {
            this.alueDao = new SqlAlueDao(this.database);
        }

        return this.alueDao;
    }

    public SqlLankaDao getLankaDao() throws SQLException {
        if (this.lankaDao == null) {
            this.lankaDao = new SqlLankaDao(this.database, getAlueDao());
        }

        return this.lankaDao;
    }

    public SqlViestiDao getViestiDao() throws SQLException {
        if (this.viestiDao == null) {
            this.viestiDao = new SqlViestiDao(this.database, getKayttajaDao(), getLankaDao());
        }

        return this.viestiDao;
    }

}
