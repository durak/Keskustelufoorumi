package keskustelufoorumi.database.sql;

import keskustelufoorumi.database.sql.SqlViestiDao;
import keskustelufoorumi.database.sql.SqlLankaDao;
import keskustelufoorumi.database.sql.SqlKayttajaDao;
import keskustelufoorumi.database.sql.SqlAlueDao;
import java.sql.SQLException;
import keskustelufoorumi.database.DaoManager;
import keskustelufoorumi.database.Database;

public class SqlDaoManager extends DaoManager  {

    private Database database;
    protected SqlAlueDao alueDao = null;
    protected SqlLankaDao lankaDao = null;
    protected SqlViestiDao viestiDao = null;
    protected SqlKayttajaDao kayttajaDao = null;

    public SqlDaoManager(Database database) {
        this.database = database;
    }

    @Override
    public SqlKayttajaDao getKayttajaDao() throws SQLException {
        if (this.kayttajaDao == null) {
            this.kayttajaDao = new SqlKayttajaDao(this.database);
        }

        return this.kayttajaDao;
    }

    @Override
    public SqlAlueDao getAlueDao() throws SQLException {
        if (this.alueDao == null) {
            this.alueDao = new SqlAlueDao(this.database);
        }

        return this.alueDao;
    }

    @Override
    public SqlLankaDao getLankaDao() throws SQLException {
        if (this.lankaDao == null) {
            this.lankaDao = new SqlLankaDao(this.database, getAlueDao());
        }

        return this.lankaDao;
    }

    @Override
    public SqlViestiDao getViestiDao() throws SQLException {
        if (this.viestiDao == null) {
            this.viestiDao = new SqlViestiDao(this.database, getKayttajaDao(), getLankaDao());
        }

        return this.viestiDao;
    }

}
