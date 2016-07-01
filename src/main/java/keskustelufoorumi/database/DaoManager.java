package keskustelufoorumi.database;

import keskustelufoorumi.database.sql.SqlDaoManager;
import java.sql.SQLException;

public abstract class DaoManager {

    public enum Tietokantatyyppi {
        SQL
    }

    public abstract AlueDao getAlueDao() throws SQLException;

    public abstract KayttajaDao getKayttajaDao() throws SQLException;

    public abstract LankaDao getLankaDao() throws SQLException;

    public abstract ViestiDao getViestiDao() throws SQLException;

    // Luodaan valitun tietokantatyypin mukainen DaoManager
    public static DaoManager getDaoManager(Database database, Tietokantatyyppi tyyppi) {
        switch (tyyppi) {
            case SQL:
                return new SqlDaoManager(database);
            default:
                return null;
        }
    }
}
